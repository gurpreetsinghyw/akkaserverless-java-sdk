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

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import com.akkaserverless.scalasdk.AkkaServerless
import com.akkaserverless.javasdk.testkit.{ AkkaServerlessTestKit => JTestKit }

/**
 * TestKit for running Akka Serverless services locally.
 *
 * <p>Requires Docker for starting a local instance of the Akka Serverless proxy.
 *
 * <p>Create an AkkaServerlessTestKit with an {@link AkkaServerless} service descriptor, and then {@link #start} the
 * testkit before testing the service with gRPC or HTTP clients. Call {@link #stop} after tests are complete.
 */
object AkkaServerlessTestKit {
  def apply(main: AkkaServerless): AkkaServerlessTestKit =
    new AkkaServerlessTestKit(new JTestKit(main.delegate))
}
class AkkaServerlessTestKit private (delegate: JTestKit) {
  def start(): AkkaServerlessTestKit = {
    delegate.start()
    this
  }

  /**
   * Get {@link GrpcClientSettings} for creating Akka gRPC clients.
   *
   * @return
   *   test gRPC client settings
   */
  def grpcClientSettings: GrpcClientSettings = delegate.getGrpcClientSettings()

  def system: ActorSystem = delegate.getActorSystem()

  def stop(): Unit = delegate.stop()
}