package org.example;

import com.akkaserverless.javasdk.DeferredCall;

// This code is managed by Akka Serverless tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * Not intended for user extension, provided through generated implementation
 */
public interface Components {
  UserByNameViewImplCalls userByNameViewImpl();

  interface UserByNameViewImplCalls {
    DeferredCall<org.example.view.UserViewModel.ByNameRequest, org.example.view.UserViewModel.UserResponse> getUserByName(org.example.view.UserViewModel.ByNameRequest byNameRequest);
  }
}
