package fr.esgi.musteat.backend;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class ApiTestBase {
    protected String getToken(String username, String password) {
        return given()
                .contentType(ContentType.URLENC)
                .param("username", username)
                .param("password", password)
                .post("/login")
                .then()
                .extract().body().jsonPath().getString("access_token");
    }
}
