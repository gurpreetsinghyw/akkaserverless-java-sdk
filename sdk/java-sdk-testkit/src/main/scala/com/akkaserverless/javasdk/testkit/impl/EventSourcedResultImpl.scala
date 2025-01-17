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

package com.akkaserverless.javasdk.testkit.impl

import com.akkaserverless.javasdk.SideEffect
import com.akkaserverless.javasdk.eventsourcedentity.EventSourcedEntity
import com.akkaserverless.javasdk.impl.DeferredCallImpl
import com.akkaserverless.javasdk.impl.effect.ErrorReplyImpl
import com.akkaserverless.javasdk.impl.effect.ForwardReplyImpl
import com.akkaserverless.javasdk.impl.effect.MessageReplyImpl
import com.akkaserverless.javasdk.impl.effect.NoReply
import com.akkaserverless.javasdk.impl.effect.NoSecondaryEffectImpl
import com.akkaserverless.javasdk.impl.effect.SecondaryEffectImpl
import com.akkaserverless.javasdk.impl.eventsourcedentity.EventSourcedEntityEffectImpl
import com.akkaserverless.javasdk.impl.eventsourcedentity.EventSourcedEntityEffectImpl.EmitEvents
import com.akkaserverless.javasdk.impl.eventsourcedentity.EventSourcedEntityEffectImpl.NoPrimaryEffect
import com.akkaserverless.javasdk.testkit.DeferredCallDetails
import com.akkaserverless.javasdk.testkit.EventSourcedResult
import com.akkaserverless.javasdk.testkit.impl.EventSourcedResultImpl.eventsOf

import java.util.Collections
import java.util.{ List => JList }
import scala.jdk.CollectionConverters._

/**
 * INTERNAL API
 */
private[akkaserverless] object EventSourcedResultImpl {
  def eventsOf(effect: EventSourcedEntity.Effect[_]): JList[Any] = {
    effect match {
      case ei: EventSourcedEntityEffectImpl[_] =>
        ei.primaryEffect match {
          case ee: EmitEvents          => ee.event.toList.asJava
          case _: NoPrimaryEffect.type => Collections.emptyList()
        }
    }
  }

  def secondaryEffectOf[S](effect: EventSourcedEntity.Effect[_], state: S): SecondaryEffectImpl = {
    effect match {
      case ei: EventSourcedEntityEffectImpl[S @unchecked] =>
        ei.secondaryEffect(state)
    }
  }

  private def toDeferredCallDetails(sideEffects: Vector[SideEffect]): JList[DeferredCallDetails[_, _]] = {
    sideEffects
      .map { sideEffect =>
        TestKitDeferredCall(sideEffect.call.asInstanceOf[DeferredCallImpl[_, _]])
          .asInstanceOf[DeferredCallDetails[_, _]] // java List is invariant in type
      }
      .toList
      .asJava
  }

}

/**
 * INTERNAL API
 */
private[akkaserverless] final class EventSourcedResultImpl[R, S](
    effect: EventSourcedEntityEffectImpl[S],
    state: S,
    secondaryEffect: SecondaryEffectImpl)
    extends EventSourcedResult[R] {
  import EventSourcedResultImpl._

  def this(effect: EventSourcedEntity.Effect[R], state: S, secondaryEffect: SecondaryEffectImpl) =
    this(effect.asInstanceOf[EventSourcedEntityEffectImpl[S]], state, secondaryEffect)

  private lazy val eventsIterator = getAllEvents().iterator

  private def secondaryEffectName: String = secondaryEffect match {
    case _: MessageReplyImpl[_] => "reply"
    case _: ForwardReplyImpl[_] => "forward"
    case _: ErrorReplyImpl[_]   => "error"
    case _: NoReply[_]          => "noReply"
    case NoSecondaryEffectImpl  => "no effect" // this should never happen
  }

  /** All emitted events. */
  override def getAllEvents(): java.util.List[Any] = eventsOf(effect)

  override def isReply: Boolean = secondaryEffect.isInstanceOf[MessageReplyImpl[_]]

  def getReply: R = secondaryEffect match {
    case MessageReplyImpl(reply, _, _) => reply.asInstanceOf[R]
    case _ => throw new IllegalStateException(s"The effect was not a reply but [$secondaryEffectName]")
  }

  override def isForward: Boolean = secondaryEffect.isInstanceOf[ForwardReplyImpl[_]]

  override def getForward: DeferredCallDetails[_, R] = secondaryEffect match {
    case ForwardReplyImpl(deferredCall: DeferredCallImpl[_, _], _) =>
      TestKitDeferredCall(deferredCall.asInstanceOf[DeferredCallImpl[_, R]])
    case _ => throw new IllegalStateException(s"The effect was not a forward but [$secondaryEffectName]")
  }

  override def isError: Boolean = secondaryEffect.isInstanceOf[ErrorReplyImpl[_]]

  override def getError: String = secondaryEffect match {
    case ErrorReplyImpl(description, _) => description
    case _ => throw new IllegalStateException(s"The effect was not an error but [$secondaryEffectName]")
  }

  override def isNoReply: Boolean = secondaryEffect.isInstanceOf[NoReply[_]]

  override def getUpdatedState: AnyRef = state.asInstanceOf[AnyRef]

  override def didEmitEvents(): Boolean = !getAllEvents().isEmpty

  override def getNextEventOfType[E](expectedClass: Class[E]): E =
    if (!eventsIterator.hasNext) throw new NoSuchElementException("No more events found")
    else {
      @SuppressWarnings(Array("unchecked")) val next = eventsIterator.next
      if (expectedClass.isInstance(next)) next.asInstanceOf[E]
      else
        throw new NoSuchElementException(
          "expected event type [" + expectedClass.getName + "] but found [" + next.getClass.getName + "]")
    }

  override def getSideEffects(): JList[DeferredCallDetails[_, _]] =
    toDeferredCallDetails(secondaryEffect.sideEffects)

}
