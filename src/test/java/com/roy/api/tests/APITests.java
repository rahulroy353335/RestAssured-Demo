package com.roy.api.tests;

import io.restassured.RestAssured;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import com.roy.base.BaseTest;
import com.roy.listeners.TestListener;
import org.testng.annotations.Listeners;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;

@Listeners(TestListener.class)
public class APITests extends BaseTest {
    @Test(enabled = false)
    public void testGetRequest() {

        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        given()
                .header("Content-Type", "application/json")
                .when()
                .get("/todos/1")
                .then()
                .statusCode(200)
                .body("title", equalTo("delectus aut autem"));
    }

    @Test(enabled = false)
    public void testPostRequest() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        String requestBody = "{\"title\":\"foo\",\"body\":\"bar\",\"userId\":1}";

        given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .body("title", equalTo("foo"));
    }

    @Test(enabled = true)
    public void testGetUser() {
        try {
            test.log(Status.INFO, "Starting GET users test");
            logger.info("Starting API test for GET /users?page=2");
            // RestAssured.baseURI = "https://reqres.in/api";
            Response response = given()
                    .header("Content-Type", "application/json", "x-api-key", "reqres-free-v1")
                    .when()
                    .get("/users?page=2")
                    .then()
                    .extract()
                    .response();

            logger.debug("API Response: \n" + response.asPrettyString());
            test.log(Status.INFO, "API Response: \n" + response.asPrettyString());
            // Verify status code
            Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
            test.log(Status.PASS, "Status code verified as 200");
            // Additional verification (recommended)
            Assert.assertFalse(response.getBody().asString().isEmpty(), "Response body should not be empty");
            test.log(Status.PASS, "Response contains user data");

        } catch (Exception e) {
            logger.error("Test failed: " + e.getMessage(), e);
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }

    }
}
