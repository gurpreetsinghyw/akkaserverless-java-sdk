package org.example

import com.akkaserverless.scalasdk.Context
import com.akkaserverless.scalasdk.DeferredCall
import com.akkaserverless.scalasdk.Metadata
import com.akkaserverless.scalasdk.impl.InternalContext
import com.akkaserverless.scalasdk.impl.ScalaDeferredCallAdapter


// This code is managed by Akka Serverless tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * Not intended for direct instantiation, called by generated code, use Action.components() to access
 */
final class ComponentsImpl(context: InternalContext) extends Components {

  def this(context: Context) =
    this(context.asInstanceOf[InternalContext])

  private def getGrpcClient[T](serviceClass: Class[T]): T =
    context.getComponentGrpcClient(serviceClass)

 @Override
 override def counter: Components.CounterCalls =
   new CounterCallsImpl();


 private final class CounterCallsImpl extends Components.CounterCalls {
   override def increase(command: _root_.org.example.valueentity.IncreaseValue): DeferredCall[_root_.org.example.valueentity.IncreaseValue, _root_.com.google.protobuf.empty.Empty] =
     ScalaDeferredCallAdapter(
       command,
       Metadata.empty,
       "org.example.valueentity.CounterService",
       "Increase",
       () => getGrpcClient(classOf[_root_.org.example.valueentity.CounterService]).increase(command)
     )
   override def decrease(command: _root_.org.example.valueentity.DecreaseValue): DeferredCall[_root_.org.example.valueentity.DecreaseValue, _root_.com.google.protobuf.empty.Empty] =
     ScalaDeferredCallAdapter(
       command,
       Metadata.empty,
       "org.example.valueentity.CounterService",
       "Decrease",
       () => getGrpcClient(classOf[_root_.org.example.valueentity.CounterService]).decrease(command)
     )
 }

}
