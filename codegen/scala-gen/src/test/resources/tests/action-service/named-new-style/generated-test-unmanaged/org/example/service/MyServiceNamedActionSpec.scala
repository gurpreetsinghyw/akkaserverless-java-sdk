package org.example.service

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.akkaserverless.scalasdk.action.Action
import com.akkaserverless.scalasdk.testkit.ActionResult
import com.google.protobuf.empty.Empty
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class MyServiceNamedActionSpec
    extends AnyWordSpec
    with Matchers {

  "MyServiceNamedAction" must {

    "have example test that can be removed" in {
      val testKit = MyServiceNamedActionTestKit(new MyServiceNamedAction(_))
      // use the testkit to execute a command
      // and verify final updated state:
      // val result = testKit.someOperation(SomeRequest)
      // verify the response
      // result.reply shouldBe expectedReply
    }

    "handle command simpleMethod" in {
      val testKit = MyServiceNamedActionTestKit(new MyServiceNamedAction(_))
      // val result = testKit.simpleMethod(MyRequest(...))
    }

    "handle command streamedOutputMethod" in {
      val testKit = MyServiceNamedActionTestKit(new MyServiceNamedAction(_))
      // val result = testKit.streamedOutputMethod(MyRequest(...))
    }

    "handle command streamedInputMethod" in {
      val testKit = MyServiceNamedActionTestKit(new MyServiceNamedAction(_))
      // val result = testKit.streamedInputMethod(Source.single(MyRequest(...)))
    }

    "handle command fullStreamedMethod" in {
      val testKit = MyServiceNamedActionTestKit(new MyServiceNamedAction(_))
      // val result = testKit.fullStreamedMethod(Source.single(MyRequest(...)))
    }

  }
}
