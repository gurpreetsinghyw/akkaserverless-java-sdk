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

package com.akkaserverless.javasdk.testkit.junit;

import akka.actor.ActorSystem;
import akka.grpc.GrpcClientSettings;
import akka.stream.Materializer;
import com.akkaserverless.javasdk.AkkaServerless;
import com.akkaserverless.javasdk.testkit.AkkaServerlessTestKit;
import org.junit.rules.ExternalResource;

/**
 * A JUnit external resource for {@link AkkaServerlessTestKit}, which automatically manages the
 * lifecycle of the testkit. The testkit will be automatically stopped when the test completes or
 * fails.
 *
 * <p><b>Note</b>: JUnit is not provided as a transitive dependency of the Java SDK testkit module
 * but must be added explicitly to your project.
 *
 * <p>Example:
 *
 * <pre>
 * import com.akkaserverless.javasdk.testkit.junit.AkkaServerlessTestKitResource;
 *
 * public class MyAkkaServerlessIntegrationTest {
 *
 *   private static final AkkaServerless MY_AKKA_SERVERLESS = new AkkaServerless(); // with registered services
 *
 *   &#64;ClassRule
 *   public static final AkkaServerlessTestKitResource testKit = new AkkaServerlessTestKitResource(MY_AKKA_SERVERLESS);
 *
 *   private final MyServiceClient client; // generated Akka gRPC client
 *
 *   public MyAkkaServerlessIntegrationTest() {
 *     this.client = MyServiceClient.create(testKit.getGrpcClientSettings(), testKit.getActorSystem());
 *   }
 *
 *   &#64;Test
 *   public void test() {
 *     // use client to test service
 *   }
 * }
 * </pre>
 */
public final class AkkaServerlessTestKitResource extends ExternalResource {

  private final AkkaServerlessTestKit testKit;

  public AkkaServerlessTestKitResource(AkkaServerless akkaServerless) {
    this(akkaServerless, AkkaServerlessTestKit.Settings.DEFAULT);
  }

  public AkkaServerlessTestKitResource(
      AkkaServerless akkaServerless, AkkaServerlessTestKit.Settings settings) {
    this.testKit = new AkkaServerlessTestKit(akkaServerless, settings);
  }

  @Override
  protected void before() {
    testKit.start();
  }

  /**
   * Get the host name/IP address where the Akka Serverless service is available. This is relevant
   * in certain Continuous Integration environments.
   *
   * @return Akka Serverless host
   */
  public String getHost() {
    return testKit.getHost();
  }

  /**
   * Get the local port where the Akka Serverless service is available.
   *
   * @return local Akka Serverless port
   */
  public int getPort() {
    return testKit.getPort();
  }

  /**
   * Get an Akka gRPC client for the given service name. The same client instance is shared for the
   * test. The lifecycle of the client is managed by the SDK and it should not be stopped by user
   * code.
   *
   * @param <T> The "service" interface generated for the service by Akka gRPC
   * @param clientClass The class of a gRPC service generated by Akka gRPC
   */
  public <T> T getGrpcClient(Class<T> clientClass) {
    return testKit.getGrpcClient(clientClass);
  }

  /**
   * An Akka Stream materializer to use for running streams. Needed for example in a command handler
   * which accepts streaming elements but returns a single async reply once all streamed elements
   * has been consumed.
   */
  public Materializer getMaterializer() {
    return testKit.getMaterializer();
  }

  /**
   * Get an {@link ActorSystem} for creating Akka HTTP clients.
   *
   * @return test actor system
   */
  public ActorSystem getActorSystem() {
    return testKit.getActorSystem();
  }

  /**
   * Get {@link GrpcClientSettings} for creating Akka gRPC clients.
   *
   * @return test gRPC client settings
   * @deprecated Use <code>getGrpcClient</code> instead.
   */
  @Deprecated(since = "0.8.1", forRemoval = true)
  public GrpcClientSettings getGrpcClientSettings() {
    return testKit.getGrpcClientSettings();
  }

  @Override
  protected void after() {
    testKit.stop();
  }
}
