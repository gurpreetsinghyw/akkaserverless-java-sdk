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

package com.akkaserverless.scalasdk.eventsourcedentity

import com.akkaserverless.scalasdk.MetadataContext

/** An event sourced command context. */
trait CommandContext extends EventSourcedEntityContext with MetadataContext {

  /**
   * The current sequence number of events in this entity.
   *
   * @return
   *   The current sequence number.
   */
  def sequenceNumber: Long

  /**
   * The name of the command being executed.
   *
   * @return
   *   The name of the command.
   */
  def commandName: String

  /**
   * The id of the command being executed.
   *
   * @return
   *   The id of the command.
   */
  def commandId: Long
}
