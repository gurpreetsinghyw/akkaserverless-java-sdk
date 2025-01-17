package org.example.unnamed.view

import com.akkaserverless.javasdk.impl.view.UpdateHandlerNotFound
import com.akkaserverless.scalasdk.impl.view.ViewRouter
import com.akkaserverless.scalasdk.view.View

// This code is managed by Akka Serverless tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

class UserByNameViewRouter(view: UserByNameView)
  extends ViewRouter[UserState, UserByNameView](view) {

  override def handleUpdate(
      eventName: String,
      state: UserState,
      event: Any): View.UpdateEffect[UserState] = {

    eventName match {
      

      case _ =>
        throw new UpdateHandlerNotFound(eventName)
    }
  }

}
