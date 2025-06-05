package com.roy.api.tests;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roy.api.pojo.ReqResPost;
import com.roy.api.pojoresponse.ResponseRespost;
import com.roy.base.BaseTest;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class APITestPojo extends BaseTest {

    private static final Logger logger = LogManager.getLogger(APITestPojo.class);

    @Test(enabled = true)
    public void testCreateUser() throws JsonProcessingException {
        // 1. Create Request POJO
        ReqResPost request = new ReqResPost();
        request.setName("morpheus");
        request.setJob("leader");

        logger.info("Request payload: {}", request);
        test.log(Status.INFO,
                "API Request: \n" + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request));
        ResponseRespost response = given()
                .contentType(ContentType.JSON)
                .header("x-api-key", "reqres-free-v1")
                .body(request)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                // Validate JSON schema
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/create-user-response-schema.json"))
                .extract()
                .as(ResponseRespost.class);
        test.log(Status.PASS, "Status code verified as 201");
        logger.info("Response received: {}", response);
        test.log(Status.INFO,
                "API Response: \n" + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));

        Assert.assertEquals(response.getName(), request.getName(), "Name mismatch");
        Assert.assertEquals(response.getJob(), request.getJob(), "Job mismatch");
        Assert.assertNotNull(response.getId(), "ID should not be null");
        Assert.assertNotNull(response.getCreatedAt(), "Creation date should not be null");

    }
}
