package fr.esgi.musteat.backend.user.controller;

import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.exposition.dto.CreateLocationDTO;
import fr.esgi.musteat.backend.location.exposition.dto.LocationDTO;
import fr.esgi.musteat.backend.user.domain.User;
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
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

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
    void should_create_user() {
        var createUserDTO = new CreateUserDTO();
        createUserDTO.name = "test";
        createUserDTO.password = "password";
        createUserDTO.location = new CreateLocationDTO();
        createUserDTO.location.latitude = 15.0;
        createUserDTO.location.longitude = 15.0;

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

        var userDTO = when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", UserDTO.class);

        assertThat(userDTO).isNotNull();
        assertThat(userDTO.name).isEqualTo("test");
        assertThat(userDTO.location.latitude).isEqualTo(15.0);
        assertThat(userDTO.location.longitude).isEqualTo(15.0);
    }

    @Test
    @Order(2)
    void should_not_create_user_with_invalid_name() {
        var createUserDTO = new CreateUserDTO();
        createUserDTO.name = null;
        createUserDTO.password = "password";
        createUserDTO.location = new CreateLocationDTO();
        createUserDTO.location.latitude = 10.0;
        createUserDTO.location.longitude = 10.0;
        given()
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
        var createUserDTO = new CreateUserDTO();
        createUserDTO.name = "test";
        createUserDTO.password = null;
        createUserDTO.location = new CreateLocationDTO();
        createUserDTO.location.latitude = 10.0;
        createUserDTO.location.longitude = 10.0;
        given()
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
        createUserDTO.name = "test";
        createUserDTO.password = "password";
        createUserDTO.location = null;
        given()
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
        var userDTOs = when()
                .get("/users")
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", new TypeRef<List<UserDTO>>() {});

        assertThat(userDTOs).hasSize(1);
    }

    @Test
    @Order(6)
    void should_retrieve_single_user() {
        var userDTO = when()
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
        updateUserDTO.name = "New Name";
        updateUserDTO.password = this.fixturesController.getUserFixture().getPassword();
        updateUserDTO.location = new CreateLocationDTO();
        updateUserDTO.location.latitude = this.fixturesController.getUserFixture().getLocation().getLatitude();
        updateUserDTO.location.longitude = this.fixturesController.getUserFixture().getLocation().getLongitude();

        var location = given()
                .contentType(ContentType.JSON)
                .body(updateUserDTO)
                .when()
                .put("/user/" + this.fixturesController.getUserFixture().getId())
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var userDTO = when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", UserDTO.class);

        User user = User.from(updateUserDTO, Location.from(updateUserDTO.location));
        user.setId(this.fixturesController.getUserFixture().getId());

        assertThat(userDTO).isEqualTo(UserDTO.from(user));
    }

    @Test
    @Order(8)
    void should_not_update_user_name_when_invalid() {
        var updateUserDTO = UserDTO.from(this.fixturesController.getUserFixture());
        updateUserDTO.name = null;

        given()
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
        updateUserDTO.name = this.fixturesController.getUserFixture().getName();
        updateUserDTO.password = this.fixturesController.getUserFixture().getPassword();
        updateUserDTO.location = new CreateLocationDTO();
        updateUserDTO.location.latitude = this.fixturesController.getUserFixture().getLocation().getLatitude();
        updateUserDTO.location.longitude = this.fixturesController.getUserFixture().getLocation().getLongitude();

        var location = given()
                .contentType(ContentType.JSON)
                .body(updateUserDTO)
                .when()
                .put("/user/" + this.fixturesController.getUserFixture().getId())
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var userDTO = when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", UserDTO.class);

        User user = User.from(updateUserDTO, Location.from(updateUserDTO.location));
        user.setId(this.fixturesController.getUserFixture().getId());

        assertThat(userDTO).isEqualTo(UserDTO.from(user));
    }

    @Test
    @Order(10)
    void should_not_update_user_location_when_invalid() {
        var updateUserDTO = UserDTO.from(this.fixturesController.getUserFixture());
        updateUserDTO.location = null;

        given()
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
                .when()
                .delete("/user/" + this.fixturesController.getUserFixture().getId())
                .then()
                .statusCode(200);

        when()
                .get("/user/" + this.fixturesController.getUserFixture().getId())
                .then()
                .statusCode(404);
    }
}
