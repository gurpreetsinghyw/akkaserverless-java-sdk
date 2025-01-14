/*
 * Copyright 2021 Lightbend Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akkaserverless.javasdk.impl.replicatedentity

import java.util.{ Set => JSet }
import java.util.{ Collection => JCollection }
import java.util.{ Collections => JCollections }

import scala.jdk.CollectionConverters._

import com.akkaserverless.javasdk.impl.AnySupport
import com.akkaserverless.javasdk.replicatedentity.ReplicatedMultiMap
import com.akkaserverless.protocol.replicated_entity.ReplicatedEntityDelta
import com.akkaserverless.protocol.replicated_entity.ReplicatedMultiMapDelta
import com.akkaserverless.protocol.replicated_entity.ReplicatedMultiMapEntryDelta
import com.akkaserverless.replicatedentity.ReplicatedData

private[akkaserverless] final class ReplicatedMultiMapImpl[K, V](
    anySupport: AnySupport,
    entries: Map[K, ReplicatedSetImpl[V]] = Map.empty[K, ReplicatedSetImpl[V]],
    removed: Set[K] = Set.empty[K],
    cleared: Boolean = false)
    extends ReplicatedMultiMap[K, V]
    with InternalReplicatedData {

  override type Self = ReplicatedMultiMapImpl[K, V]
  override val name = "ReplicatedMultiMap"

  /** for Scala SDK */
  def getValuesSet(key: K): Set[V] =
    entries.get(key).map(_.elementsSet).getOrElse(Set.empty[V])

  override def get(key: K): JSet[V] = entries.get(key).fold(JCollections.emptySet[V])(_.elements)

  override def put(key: K, value: V): ReplicatedMultiMapImpl[K, V] = {
    val values = entries.getOrElse(key, new ReplicatedSetImpl[V](anySupport))
    val updated = values.add(value)
    new ReplicatedMultiMapImpl(anySupport, entries.updated(key, updated), removed, cleared)
  }

  /** for Scala SDK */
  def putAll(key: K, values: Iterable[V]): ReplicatedMultiMapImpl[K, V] =
    values.foldLeft(this) { case (map, value) => map.put(key, value) }

  override def putAll(key: K, values: JCollection[V]): ReplicatedMultiMapImpl[K, V] =
    putAll(key, values.asScala)

  override def remove(key: K, value: V): ReplicatedMultiMapImpl[K, V] = {
    entries.get(key).fold(this) { values =>
      val updated = values.remove(value)
      if (updated.isEmpty) removeAll(key)
      else new ReplicatedMultiMapImpl(anySupport, entries.updated(key, updated), removed, cleared)
    }
  }

  override def removeAll(key: K): ReplicatedMultiMapImpl[K, V] = {
    if (!entries.contains(key)) {
      this
    } else {
      new ReplicatedMultiMapImpl(anySupport, entries.removed(key), removed + key, cleared)
    }
  }

  override def clear(): ReplicatedMultiMapImpl[K, V] =
    new ReplicatedMultiMapImpl[K, V](anySupport, cleared = true)

  override def size: Int = entries.values.map(_.size).sum

  override def isEmpty: Boolean = entries.isEmpty

  override def containsKey(key: K): Boolean = entries.contains(key)

  override def containsValue(key: K, value: V): Boolean =
    entries.get(key).fold(false)(_.contains(value))

  /** for Scala SDK */
  def keys: Set[K] = entries.keySet

  override def keySet: JSet[K] = entries.keySet.asJava

  override def hasDelta: Boolean = cleared || removed.nonEmpty || entries.values.exists(_.hasDelta)

  override def getDelta: ReplicatedEntityDelta.Delta =
    ReplicatedEntityDelta.Delta.ReplicatedMultiMap(
      ReplicatedMultiMapDelta(
        cleared = cleared,
        removed = removed.map(anySupport.encodeScala).toSeq,
        updated = entries.collect {
          case (key, values) if values.hasDelta =>
            ReplicatedMultiMapEntryDelta(Some(anySupport.encodeScala(key)), values.getDelta.replicatedSet)
        }.toSeq))

  override def resetDelta(): ReplicatedMultiMapImpl[K, V] =
    if (hasDelta) new ReplicatedMultiMapImpl(anySupport, entries.view.mapValues(_.resetDelta()).toMap) else this

  override val applyDelta: PartialFunction[ReplicatedEntityDelta.Delta, ReplicatedMultiMapImpl[K, V]] = {
    case ReplicatedEntityDelta.Delta.ReplicatedMultiMap(ReplicatedMultiMapDelta(cleared, removed, updated, _)) =>
      val reducedEntries =
        if (cleared) Map.empty[K, ReplicatedSetImpl[V]]
        else entries -- removed.map(key => anySupport.decodePossiblyPrimitive(key).asInstanceOf[K])
      val updatedEntries = updated.foldLeft(reducedEntries) {
        case (map, ReplicatedMultiMapEntryDelta(Some(encodedKey), Some(delta), _)) =>
          val key = anySupport.decodePossiblyPrimitive(encodedKey).asInstanceOf[K]
          val values = map.getOrElse(key, new ReplicatedSetImpl[V](anySupport))
          map.updated(key, values.applyDelta(ReplicatedEntityDelta.Delta.ReplicatedSet(delta)))
        case (map, _) => map
      }
      new ReplicatedMultiMapImpl(anySupport, updatedEntries)
  }

  override def toString = s"ReplicatedMultiMap(${entries.map { case (k, v) => s"$k->$v" }.mkString(",")})"

}
