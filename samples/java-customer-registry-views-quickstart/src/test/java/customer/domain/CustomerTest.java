/* This code was generated by Akka Serverless tooling.
 * As long as this file exists it will not be re-generated.
 * You are free to make changes to this file.
 */
package customer.domain;

import com.akkaserverless.javasdk.testkit.ValueEntityResult;
import com.akkaserverless.javasdk.valueentity.ValueEntity;
import com.akkaserverless.javasdk.valueentity.ValueEntityContext;
import com.google.protobuf.Empty;
import customer.api.CustomerApi;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;
import scala.jdk.javaapi.CollectionConverters;

import static org.junit.Assert.*;

public class CustomerTest {

  @Test
  public void exampleTest() {
    CustomerTestKit testKit = CustomerTestKit.of(Customer::new);
    // use the testkit to execute a command
    // of events emitted, or a final updated state:
    // ValueEntityResult<SomeResponse> result = testKit.someOperation(SomeRequest);
    // verify the response
    // SomeResponse actualResponse = result.getReply();
    // assertEquals(expectedResponse, actualResponse);
    // verify the final state after the command
    // assertEquals(expectedState, testKit.getState());
  }

  @Test
  public void createTest() {
    CustomerTestKit testKit = CustomerTestKit.of(Customer::new);
    // ValueEntityResult<Empty> result = testKit.create(Customer.newBuilder()...build());
  }


  @Test
  public void getCustomerTest() {
    CustomerTestKit testKit = CustomerTestKit.of(Customer::new);
    // ValueEntityResult<Customer> result = testKit.getCustomer(GetCustomerRequest.newBuilder()...build());
  }


  @Test
  public void changeNameTest() {
    CustomerTestKit testKit = CustomerTestKit.of(Customer::new);
    // ValueEntityResult<Empty> result = testKit.changeName(ChangeNameRequest.newBuilder()...build());
  }


  @Test
  public void changeAddressTest() {
    CustomerTestKit testKit = CustomerTestKit.of(Customer::new);
    // ValueEntityResult<Empty> result = testKit.changeAddress(ChangeAddressRequest.newBuilder()...build());
  }

}
