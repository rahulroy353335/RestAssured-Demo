package com.roy.api.clients;

import com.roy.api.models.User;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String USERS_PATH = "/api/users";

    public Response getUsers() {
        return given().get(USERS_PATH);
    }

    public Response getUser(int userId) {
        return given().get(USERS_PATH + "/" + userId);
    }

    public Response createUser(User user) {
        RequestSpecification request = given()
                .contentType("application/json")
                .body(user);

        return request.post(USERS_PATH);
    }

}
