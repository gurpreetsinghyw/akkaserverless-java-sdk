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

package com.lightbend.akkasls.codegen
package java

class MainSourceGeneratorSuite extends munit.FunSuite {
  private val testData = TestData.javaStyle

  def domainType(name: String): ModelBuilder.TypeArgument =
    ModelBuilder.TypeArgument(
      name,
      testData.domainProto(),
      TestData.guessDescriptor(testData.domainProto().name, testData.domainProto()))

  test("main source") {
    val mainPackageName = "com.example.service"
    val mainClassName = "SomeMain"

    val entities = Map(
      "com.example.Entity1" -> testData.eventSourcedEntity(suffix = "1"),
      "com.example.Entity2" -> testData.valueEntity(suffix = "2"),
      "com.example.Entity3" -> testData.eventSourcedEntity(suffix = "3"),
      "com.example.Entity6" -> testData.replicatedEntity(
        ModelBuilder.ReplicatedSet(domainType("SomeElement")),
        suffix = "6"))

    val services = Map("com.example.Service1" -> testData.simpleActionService())

    val generatedSrc = MainSourceGenerator.mainSource(mainPackageName, mainClassName, entities, services)
    assertNoDiff(
      generatedSrc,
      """package com.example.service;
         |
         |import com.akkaserverless.javasdk.AkkaServerless;
         |import com.example.service.domain.MyEntity1;
         |import com.example.service.domain.MyEntity3;
         |import com.example.service.domain.MyReplicatedEntity6;
         |import com.example.service.domain.MyValueEntity2;
         |import org.slf4j.Logger;
         |import org.slf4j.LoggerFactory;
         |
         |// This class was initially generated based on the .proto definition by Akka Serverless tooling.
         |//
         |// As long as this file exists it will not be overwritten: you can maintain it yourself,
         |// or delete it so it is regenerated as needed.
         |
         |public final class SomeMain {
         |
         |  private static final Logger LOG = LoggerFactory.getLogger(SomeMain.class);
         |
         |  public static AkkaServerless createAkkaServerless() {
         |    // The AkkaServerlessFactory automatically registers any generated Actions, Views or Entities,
         |    // and is kept up-to-date with any changes in your protobuf definitions.
         |    // If you prefer, you may remove this and manually register these components in a
         |    // `new AkkaServerless()` instance.
         |    return AkkaServerlessFactory.withComponents(
         |      MyEntity1::new,
         |      MyEntity3::new,
         |      MyReplicatedEntity6::new,
         |      MyValueEntity2::new,
         |      MyServiceAction::new);
         |  }
         |
         |  public static void main(String[] args) throws Exception {
         |    LOG.info("starting the Akka Serverless service");
         |    createAkkaServerless().start();
         |  }
         |}
         |""".stripMargin)
  }

  test("generated component registration source") {
    val service1Proto = testData.serviceProto("1")
    val service2Proto = testData.serviceProto("2")
    val service3Proto = testData.serviceProto("3").copy(protoPackage = "com.example.service.something")
    val service4Proto = testData.serviceProto("4").copy(protoPackage = "com.example.service.view")
    val service5Proto = testData.serviceProto("5")
    val service6Proto = testData.serviceProto("6")

    val services = Map(
      "com.example.Service1" -> testData.simpleEntityService(service1Proto, "1"),
      "com.example.Service2" -> testData.simpleEntityService(service2Proto, "2"),
      "com.example.Service3" -> testData.simpleEntityService(service3Proto, "3"),
      "com.example.Service4" -> testData.simpleViewService(service4Proto, "4"),
      "com.example.Service5" -> testData.simpleActionService(service5Proto),
      "com.example.Service6" -> testData.simpleEntityService(service6Proto, "6"))

    val entities = Map(
      "com.example.Entity1" -> testData.eventSourcedEntity(suffix = "1"),
      "com.example.Entity2" -> testData.valueEntity(suffix = "2"),
      "com.example.Entity3" -> testData.eventSourcedEntity(suffix = "3"),
      "com.example.Entity6" -> testData.replicatedEntity(
        ModelBuilder.ReplicatedSet(domainType("SomeElement")),
        suffix = "6"))

    val mainPackageName = "com.example.service"

    val generatedSrc =
      MainSourceGenerator.akkaServerlessFactorySource(mainPackageName, ModelBuilder.Model(services, entities))
    assertNoDiff(
      generatedSrc,
      """package com.example.service;
        |
        |import com.akkaserverless.javasdk.AkkaServerless;
        |import com.akkaserverless.javasdk.action.ActionCreationContext;
        |import com.akkaserverless.javasdk.eventsourcedentity.EventSourcedEntityContext;
        |import com.akkaserverless.javasdk.replicatedentity.ReplicatedEntityContext;
        |import com.akkaserverless.javasdk.valueentity.ValueEntityContext;
        |import com.akkaserverless.javasdk.view.ViewCreationContext;
        |import com.example.service.domain.MyEntity1;
        |import com.example.service.domain.MyEntity1Provider;
        |import com.example.service.domain.MyEntity3;
        |import com.example.service.domain.MyEntity3Provider;
        |import com.example.service.domain.MyReplicatedEntity6;
        |import com.example.service.domain.MyReplicatedEntity6Provider;
        |import com.example.service.domain.MyValueEntity2;
        |import com.example.service.domain.MyValueEntity2Provider;
        |import com.example.service.something.ServiceOuterClass3;
        |import com.example.service.view.MyService4ViewImpl;
        |import com.example.service.view.MyService4ViewProvider;
        |import com.example.service.view.ServiceOuterClass4;
        |import java.util.function.Function;
        |
        |// This code is managed by Akka Serverless tooling.
        |// It will be re-generated to reflect any changes to your protobuf definitions.
        |// DO NOT EDIT
        |
        |public final class AkkaServerlessFactory {
        |
        |  public static AkkaServerless withComponents(
        |      Function<EventSourcedEntityContext, MyEntity1> createMyEntity1,
        |      Function<EventSourcedEntityContext, MyEntity3> createMyEntity3,
        |      Function<ReplicatedEntityContext, MyReplicatedEntity6> createMyReplicatedEntity6,
        |      Function<ValueEntityContext, MyValueEntity2> createMyValueEntity2,
        |      Function<ViewCreationContext, MyService4ViewImpl> createMyService4ViewImpl,
        |      Function<ActionCreationContext, MyService5Action> createMyService5Action) {
        |    AkkaServerless akkaServerless = new AkkaServerless();
        |    return akkaServerless
        |      .register(MyEntity1Provider.of(createMyEntity1))
        |      .register(MyEntity3Provider.of(createMyEntity3))
        |      .register(MyReplicatedEntity6Provider.of(createMyReplicatedEntity6))
        |      .register(MyService4ViewProvider.of(createMyService4ViewImpl))
        |      .register(MyService5ActionProvider.of(createMyService5Action))
        |      .register(MyValueEntity2Provider.of(createMyValueEntity2));
        |  }
        |}
        |""".stripMargin)
  }

  test("generated component registration source for a view without update handlers") {
    val serviceProto = testData.serviceProto().copy(protoPackage = "com.example.service.view")

    val services = Map("com.example.Service" -> testData.simpleViewService(serviceProto).copy(transformedUpdates = Nil))

    val entities = Map.empty[String, ModelBuilder.Entity]

    val mainPackageName = "com.example.service"
    val mainClassName = "SomeMain"

    val generatedSrc =
      MainSourceGenerator.akkaServerlessFactorySource(mainPackageName, ModelBuilder.Model(services, entities))
    assertNoDiff(
      generatedSrc,
      """package com.example.service;
        |
        |import com.akkaserverless.javasdk.AkkaServerless;
        |import com.akkaserverless.javasdk.view.ViewCreationContext;
        |import com.example.service.view.MyServiceViewImpl;
        |import com.example.service.view.MyServiceViewProvider;
        |import com.example.service.view.ServiceOuterClass;
        |import java.util.function.Function;
        |
        |// This code is managed by Akka Serverless tooling.
        |// It will be re-generated to reflect any changes to your protobuf definitions.
        |// DO NOT EDIT
        |
        |public final class AkkaServerlessFactory {
        |
        |  public static AkkaServerless withComponents(
        |      Function<ViewCreationContext, MyServiceViewImpl> createMyServiceViewImpl) {
        |    AkkaServerless akkaServerless = new AkkaServerless();
        |    return akkaServerless
        |      .register(MyServiceViewProvider.of(createMyServiceViewImpl));
        |  }
        |}
        |""".stripMargin)
  }

}