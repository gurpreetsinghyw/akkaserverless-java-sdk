package org.example.eventsourcedentity;

import com.akkaserverless.javasdk.eventsourcedentity.EventSourcedEntity;
import com.akkaserverless.javasdk.eventsourcedentity.EventSourcedEntity.Effect;
import com.akkaserverless.javasdk.eventsourcedentity.EventSourcedEntityContext;
import com.google.protobuf.Empty;
import org.example.eventsourcedentity.domain.CounterDomain;

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
// This is the implementation for the Event Sourced Entity Service described in your counter_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class CounterServiceEntity extends AbstractCounterServiceEntity {

  @SuppressWarnings("unused")
  private final String entityId;

  public CounterServiceEntity(EventSourcedEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public CounterDomain.CounterState emptyState() {
    throw new UnsupportedOperationException("Not implemented yet, replace with your empty entity state");
  }

  @Override
  public Effect<Empty> increase(CounterDomain.CounterState currentState, CounterApi.IncreaseValue increaseValue) {
    return effects().error("The command handler for `Increase` is not implemented, yet");
  }

  @Override
  public Effect<Empty> decrease(CounterDomain.CounterState currentState, CounterApi.DecreaseValue decreaseValue) {
    return effects().error("The command handler for `Decrease` is not implemented, yet");
  }

  @Override
  public CounterDomain.CounterState increased(CounterDomain.CounterState currentState, CounterDomain.Increased increased) {
    throw new RuntimeException("The event handler for `Increased` is not implemented, yet");
  }
  @Override
  public CounterDomain.CounterState decreased(CounterDomain.CounterState currentState, CounterDomain.Decreased decreased) {
    throw new RuntimeException("The event handler for `Decreased` is not implemented, yet");
  }

}
