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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@PactConsumerTest
public class CreateItemPactTest {

    @Pact(provider = "ItemProvider", consumer = "ItemConsumer")
    public V4Pact saveItem(PactDslWithProvider builder) {
        // Headers for the POST request
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // Body of the request (what the consumer will send)
        PactDslJsonBody requestBody = new PactDslJsonBody()
                .stringType("id", "1234")
                .stringType("name", "New Item")
                .stringType("description", "A new item description");

        // Body of the response (what the provider should return)
        PactDslJsonBody responseBody = new PactDslJsonBody()
                .stringType("id", "1234")
                .stringType("name", "New Item")
                .stringType("description", "A new item description");

        return builder
                .given("Item creation")
                .uponReceiving("A request to create an item")
                .path("/item")
                .method("POST")
                .headers(headers)
                .body(requestBody)
                .willRespondWith()
                .status(201) // HTTP 201 Created
                .headers(headers)
                .body(responseBody)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(providerName = "ItemProvider") // Use a mock server on port 8080
    void testCreateItem(MockServer mockServer) {
        // Define the request body
        String requestBody = "{ \"id\": \"1234\", \"name\": \"New Item\", \"description\": \"A new item description\" }";

        // Send the POST request to the mock server
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(requestBody)
                .post(mockServer.getUrl() + "/item")
                .thenReturn();

        // Verify the response
        assertEquals(201, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1234", response.jsonPath().getString("id"));
        assertEquals("New Item", response.jsonPath().getString("name"));
        assertEquals("A new item description", response.jsonPath().getString("description"));
    }
}
