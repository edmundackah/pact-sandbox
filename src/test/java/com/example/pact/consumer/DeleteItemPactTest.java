package com.example.pact.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@PactConsumerTest
public class DeleteItemPactTest {

    @Pact(provider = "ItemProvider", consumer = "ItemConsumer")
    public V4Pact deleteItem(PactDslWithProvider builder) {
        return builder
                .given("Item 3 deletion")
                .uponReceiving("A request to delete item 3")
                .path("/item/3")
                .method("DELETE")
                .willRespondWith()
                .status(200)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "deleteItem")
    void testDeleteItem(MockServer mockServer) {
        given()
                .delete(mockServer.getUrl() + "/item/3")
                .then()
                .assertThat()
                .statusCode(200);
    }
}
