package fr.esgi.musteat.backend.user.exposition.controller;

import fr.esgi.musteat.backend.ApiTestBase;
import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import fr.esgi.musteat.backend.location.exposition.dto.CreateLocationDTO;
import fr.esgi.musteat.backend.user.exposition.dto.CreateUserDTO;
import fr.esgi.musteat.backend.user.exposition.dto.UserDTO;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.event.annotation.AfterTestClass;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest extends ApiTestBase {

    @LocalServerPort
    private int port;

    private String jwt;

    @Autowired
    private FixturesController fixturesController;


    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        this.jwt = this.getToken(fixturesController.getUserFixture().getName(), fixturesController.getUserFixture().getPassword());
        System.out.println("JWT: " + this.jwt);
    }

    @AfterTestClass
    public void tearDown() {
        fixturesController.resetFixtures();
    }

    @Test
    @Order(1)
    void should_create_user() {
        var userAddress = "1 rue de la paix, 75001 Paris";
        var createUserDTO = new CreateUserDTO();
        createUserDTO.username = "test";
        createUserDTO.password = "password";
        createUserDTO.location = new CreateLocationDTO();
        createUserDTO.location.address = userAddress;

        var location = given()
                .contentType(ContentType.JSON)
                .body(createUserDTO)
                .when()
                .post("/user")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var user_token = this.getToken(createUserDTO.username, createUserDTO.password);

        var userDTO = given()
                .headers("Authorization", "Bearer " + user_token)
        .when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", UserDTO.class);

        assertThat(userDTO).isNotNull();
        assertThat(userDTO.name).isEqualTo("test");
        assertThat(userDTO.location.latitude).isEqualTo(this.fixturesController.getLocationFixtureFromAddress(userAddress).getLatitude());
        assertThat(userDTO.location.longitude).isEqualTo(this.fixturesController.getLocationFixtureFromAddress(userAddress).getLongitude());
    }

    @Test
    @Order(2)
    void should_not_create_user_with_invalid_name() {
        var userAddress = "1 rue de la paix, 75001 Paris";
        var createUserDTO = new CreateUserDTO();
        createUserDTO.username = null;
        createUserDTO.password = "password";
        createUserDTO.location = new CreateLocationDTO();
        createUserDTO.location.address = userAddress;
        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createUserDTO)
                .when()
                .post("/user")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(3)
    void should_not_create_user_with_invalid_password() {
        var userAddress = "1 rue de la paix, 75001 Paris";
        var createUserDTO = new CreateUserDTO();
        createUserDTO.username = "test";
        createUserDTO.password = null;
        createUserDTO.location = new CreateLocationDTO();
        createUserDTO.location.address = userAddress;
        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createUserDTO)
                .when()
                .post("/user")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(4)
    void should_not_create_user_with_invalid_location() {
        var createUserDTO = new CreateUserDTO();
        createUserDTO.username = "test";
        createUserDTO.password = "password";
        createUserDTO.location = null;
        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createUserDTO)
                .when()
                .post("/user")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(5)
    void should_retrieve_bootstrapped_users() {
        var userDTOs = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get("/users")
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", new TypeRef<List<UserDTO>>() {});

        assertThat(userDTOs).hasSize(2);
    }

    @Test
    @Order(6)
    void should_retrieve_single_user() {
        var userDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get("/user/" + this.fixturesController.getUserFixture().getId())
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", UserDTO.class);

        assertThat(userDTO).isEqualTo(UserDTO.from(this.fixturesController.getUserFixture()));
    }

    @Test
    @Order(7)
    void should_update_user_name() {
        var updateUserDTO = new CreateUserDTO();
        updateUserDTO.username = "New Name";
        updateUserDTO.password = this.fixturesController.getUserFixture().getPassword();
        updateUserDTO.location = new CreateLocationDTO();
        updateUserDTO.location.address = "242 rue du faubourg Saint-Antoine, 75012 Paris";

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateUserDTO)
                .when()
                .put("/user/" + this.fixturesController.getUserFixture().getId())
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var userDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", UserDTO.class);


        assertThat(userDTO.name).isEqualTo(updateUserDTO.username);
        this.fixturesController.cleanUserFixtures();
    }

    @Test
    @Order(8)
    void should_not_update_user_name_when_invalid() {
        var updateUserDTO = new CreateUserDTO();
        updateUserDTO.username = null;
        updateUserDTO.password = this.fixturesController.getUserFixture().getPassword();
        updateUserDTO.location = new CreateLocationDTO();
        updateUserDTO.location.address = "242 rue du faubourg Saint-Antoine, 75012 Paris";

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateUserDTO)
                .when()
                .put("/user/" + this.fixturesController.getUserFixture().getId())
                .then()
                .statusCode(400);
    }

    @Test
    @Order(9)
    void should_update_user_location() {
        var updateUserDTO = new CreateUserDTO();
        updateUserDTO.username = this.fixturesController.getUserFixture().getName();
        updateUserDTO.password = this.fixturesController.getUserFixture().getPassword();
        updateUserDTO.location = new CreateLocationDTO();
        updateUserDTO.location.address = "1 rue de la paix, 75001 Paris";

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateUserDTO)
                .when()
                .put("/user/" + this.fixturesController.getUserFixture().getId())
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var userDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", UserDTO.class);

        assertThat(userDTO.name).isEqualTo(updateUserDTO.username);
        assertThat(userDTO.location.latitude).isEqualTo(this.fixturesController.getLocationFixtureFromAddress(updateUserDTO.location.address).getLatitude());
        assertThat(userDTO.location.longitude).isEqualTo(this.fixturesController.getLocationFixtureFromAddress(updateUserDTO.location.address).getLongitude());
        this.fixturesController.cleanUserFixtures();
    }

    @Test
    @Order(10)
    void should_not_update_user_location_when_invalid() {
        var updateUserDTO = new CreateUserDTO();
        updateUserDTO.username = this.fixturesController.getUserFixture().getName();
        updateUserDTO.password = this.fixturesController.getUserFixture().getPassword();
        updateUserDTO.location = null;

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateUserDTO)
                .when()
                .put("/user/" + this.fixturesController.getUserFixture().getId())
                .then()
                .statusCode(400);
    }

    @Test
    @Order(11)
    void should_delete_user() {
        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .when()
                .delete("/user/" + this.fixturesController.getUserFixture().getId())
                .then()
                .statusCode(200);

        given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get("/user/" + this.fixturesController.getUserFixture().getId())
                .then()
                .statusCode(404);
    }
}
