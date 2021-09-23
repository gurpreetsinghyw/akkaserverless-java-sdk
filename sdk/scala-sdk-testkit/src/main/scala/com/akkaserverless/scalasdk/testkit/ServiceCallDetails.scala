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

package com.akkaserverless.scalasdk.testkit

import com.akkaserverless.scalasdk.Metadata

trait ServiceCallDetails[T] {
  /** @return The forwarded message */
  def getMessage: T

  /** @return Any metadata attached to the call */
  def getMetadata: Metadata

  /** @return The name of the service being called */
  def getServiceName: String

  /** @return The method name being called */
  def getMethodName: String
}