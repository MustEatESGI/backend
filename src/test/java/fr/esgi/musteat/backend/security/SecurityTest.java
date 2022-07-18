package fr.esgi.musteat.backend.security;

import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.event.annotation.AfterTestClass;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SecurityTest {

    private static String USERNAME;
    private static String PASSWORD;

    @LocalServerPort
    private int port;

    @Autowired
    private FixturesController fixturesController;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @AfterTestClass
    public void tearDown() {
        fixturesController.resetFixtures();
    }

    @Test
    @Order(1)
    void should_create_jwt() {
        var token = given()
                .contentType(ContentType.URLENC)
                .param("username", fixturesController.getUserFixture().getName())
                .param("password", fixturesController.getUserFixture().getPassword())
                .post("/login")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getString("access_token");

        assertThat(token).isNotNull();
    }
}