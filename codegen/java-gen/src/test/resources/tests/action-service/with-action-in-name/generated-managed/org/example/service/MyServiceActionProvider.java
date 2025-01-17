package org.example.service;

import com.akkaserverless.javasdk.action.ActionCreationContext;
import com.akkaserverless.javasdk.action.ActionOptions;
import com.akkaserverless.javasdk.action.ActionProvider;
import com.akkaserverless.javasdk.impl.action.ActionRouter;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Empty;
import com.google.protobuf.EmptyProto;

import java.util.function.Function;

// This code is managed by Akka Serverless tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * MyServiceActionProvider that defines how to register and create the action for
 * the Protobuf service <code>MyServiceAction</code>.
 *
 * Should be used with the <code>register</code> method in {@link com.akkaserverless.javasdk.AkkaServerless}.
 */
public class MyServiceActionProvider implements ActionProvider<MyServiceActionImpl> {

  private final Function<ActionCreationContext, MyServiceActionImpl> actionFactory;
  private final ActionOptions options;

  /** Factory method of MyServiceActionProvider */
  public static MyServiceActionProvider of(Function<ActionCreationContext, MyServiceActionImpl> actionFactory) {
    return new MyServiceActionProvider(actionFactory, ActionOptions.defaults());
  }

  private MyServiceActionProvider(Function<ActionCreationContext, MyServiceActionImpl> actionFactory, ActionOptions options) {
    this.actionFactory = actionFactory;
    this.options = options;
  }

  @Override
  public final ActionOptions options() {
    return options;
  }

  public final MyServiceActionProvider withOptions(ActionOptions options) {
    return new MyServiceActionProvider(actionFactory, options);
  }

  @Override
  public final Descriptors.ServiceDescriptor serviceDescriptor() {
    return ServiceOuterClass.getDescriptor().findServiceByName("MyServiceAction");
  }

  @Override
  public final MyServiceActionRouter newRouter(ActionCreationContext context) {
    return new MyServiceActionRouter(actionFactory.apply(context));
  }

  @Override
  public final Descriptors.FileDescriptor[] additionalDescriptors() {
    return new Descriptors.FileDescriptor[] {
      EmptyProto.getDescriptor(),
      ServiceOuterClass.getDescriptor()
    };
  }

}
