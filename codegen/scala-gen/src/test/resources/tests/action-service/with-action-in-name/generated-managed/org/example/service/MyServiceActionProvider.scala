package org.example.service

import com.akkaserverless.scalasdk.action.ActionCreationContext
import com.akkaserverless.scalasdk.action.ActionOptions
import com.akkaserverless.scalasdk.action.ActionProvider
import com.google.protobuf.Descriptors

import scala.collection.immutable.Seq

// This code is managed by Akka Serverless tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

object MyServiceActionProvider {
  def apply(actionFactory: ActionCreationContext => MyServiceActionImpl): MyServiceActionProvider =
    new MyServiceActionProvider(actionFactory, ActionOptions.defaults)

  def apply(actionFactory: ActionCreationContext => MyServiceActionImpl, options: ActionOptions): MyServiceActionProvider =
    new MyServiceActionProvider(actionFactory, options)
}

class MyServiceActionProvider private(actionFactory: ActionCreationContext => MyServiceActionImpl,
                                      override val options: ActionOptions)
  extends ActionProvider[MyServiceActionImpl] {

  override final def serviceDescriptor: Descriptors.ServiceDescriptor =
    ExampleActionProto.javaDescriptor.findServiceByName("MyServiceAction")

  override final def newRouter(context: ActionCreationContext): MyServiceActionRouter =
    new MyServiceActionRouter(actionFactory(context))

  override final def additionalDescriptors: Seq[Descriptors.FileDescriptor] =
    ExampleActionProto.javaDescriptor ::
    Nil

  def withOptions(options: ActionOptions): MyServiceActionProvider =
    new MyServiceActionProvider(actionFactory, options)
}

