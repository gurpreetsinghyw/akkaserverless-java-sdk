package org.example.service

import com.akkaserverless.scalasdk.action.ActionCreationContext
import com.akkaserverless.scalasdk.action.ActionOptions
import com.akkaserverless.scalasdk.action.ActionProvider
import com.google.protobuf.Descriptors

import scala.collection.immutable.Seq

// This code is managed by Akka Serverless tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

object MyServiceNamedActionProvider {
  def apply(actionFactory: ActionCreationContext => MyServiceNamedAction): MyServiceNamedActionProvider =
    new MyServiceNamedActionProvider(actionFactory, ActionOptions.defaults)

  def apply(actionFactory: ActionCreationContext => MyServiceNamedAction, options: ActionOptions): MyServiceNamedActionProvider =
    new MyServiceNamedActionProvider(actionFactory, options)
}

class MyServiceNamedActionProvider private(actionFactory: ActionCreationContext => MyServiceNamedAction,
                                      override val options: ActionOptions)
  extends ActionProvider[MyServiceNamedAction] {

  override final def serviceDescriptor: Descriptors.ServiceDescriptor =
    ExampleActionProto.javaDescriptor.findServiceByName("MyService")

  override final def newRouter(context: ActionCreationContext): MyServiceNamedActionRouter =
    new MyServiceNamedActionRouter(actionFactory(context))

  override final def additionalDescriptors: Seq[Descriptors.FileDescriptor] =
    ExampleActionProto.javaDescriptor ::
    Nil

  def withOptions(options: ActionOptions): MyServiceNamedActionProvider =
    new MyServiceNamedActionProvider(actionFactory, options)
}

