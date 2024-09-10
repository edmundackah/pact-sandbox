package com.example.pact.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PactConsumerTest
class GetItemPactTest {

    @Pact(provider = "ItemProvider", consumer = "ItemConsumer")
    public V4Pact getItem(PactDslWithProvider builder) {
        return builder
                .given("An item exists with id 1")
                .uponReceiving("A request to get an item")
                .path("/item/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .stringType("id", "1")
                        .stringType("name", "Item Name")
                        .stringType("description", "Item Description"))
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "getItem")
    void testGetItem(MockServer mockServer) {
        // Call the endpoint and verify response
        Response response = RestAssured
                .get(mockServer.getUrl() + "/item/1")
                .thenReturn();

        assertEquals(200, response.statusCode());
        assertEquals("1", response.jsonPath().getString("id"));
    }
}
