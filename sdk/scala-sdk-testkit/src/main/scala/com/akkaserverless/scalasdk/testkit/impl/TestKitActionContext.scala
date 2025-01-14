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

package com.akkaserverless.scalasdk.testkit.impl

import akka.stream.Materializer
import com.akkaserverless.scalasdk.Metadata
import com.akkaserverless.scalasdk.action.ActionContext
import com.akkaserverless.scalasdk.action.ActionCreationContext

/**
 * INTERNAL API Used by the generated testkit
 */
final class TestKitActionContext extends AbstractTestKitContext with ActionContext with ActionCreationContext {
  override def metadata: Metadata = throw new UnsupportedOperationException(
    "Accessing metadata from testkit not supported yet")
  override def eventSubject: Option[String] = Some("test-subject-id")
  override def getGrpcClient[T](clientClass: Class[T], service: String): T =
    throw new UnsupportedOperationException("Testing logic using a gRPC client is not possible with the testkit")

}
