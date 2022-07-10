package fr.esgi.musteat.backend.restaurant.exposition.controller;

import fr.esgi.musteat.backend.ApiTestBase;
import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import fr.esgi.musteat.backend.location.exposition.dto.CreateLocationDTO;
import fr.esgi.musteat.backend.restaurant.exposition.dto.CreateRestaurantDTO;
import fr.esgi.musteat.backend.restaurant.exposition.dto.RestaurantDetailsDTO;
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
public class RestaurantControllerTest extends ApiTestBase {

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
    }

    @AfterTestClass
    public void tearDown() {
        fixturesController.resetFixtures();
    }

    @Test
    @Order(1)
    void should_create_restaurant() {
        var restaurantAddress = "1 rue de la paix, 75001 Paris";
        var createRestaurantDTO = new CreateRestaurantDTO();
        createRestaurantDTO.name = "testRestaurant";
        createRestaurantDTO.location = new CreateLocationDTO();
        createRestaurantDTO.location.address = restaurantAddress;

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createRestaurantDTO)
        .when()
                .post("/restaurant")
        .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var restaurantDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get(location)
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", RestaurantDetailsDTO.class);

        assertThat(restaurantDTO).isNotNull();
        assertThat(restaurantDTO.name).isEqualTo("testRestaurant");
        assertThat(restaurantDTO.location.latitude).isEqualTo(this.fixturesController.getLocationFixtureFromAddress(restaurantAddress).getLatitude());
        assertThat(restaurantDTO.location.longitude).isEqualTo(this.fixturesController.getLocationFixtureFromAddress(restaurantAddress).getLongitude());
        assertThat(restaurantDTO.meals).isEmpty();
    }

    @Test
    @Order(2)
    void should_not_create_restaurant_with_invalid_name() {
        var restaurantAddress = "1 rue de la paix, 75001 Paris";
        var createRestaurantDTO = new CreateRestaurantDTO();
        createRestaurantDTO.name = null;
        createRestaurantDTO.location = new CreateLocationDTO();
        createRestaurantDTO.location.address = restaurantAddress;
        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createRestaurantDTO)
                .when()
                .post("/restaurant")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(3)
    void should_not_create_restaurant_with_invalid_location() {
        var createRestaurantDTO = new CreateRestaurantDTO();
        createRestaurantDTO.name = "testRestaurant";
        createRestaurantDTO.location = null;
        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createRestaurantDTO)
                .when()
                .post("/restaurant")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(4)
    void should_retrieve_bootstrapped_restaurants() {
        var restaurantDTOs = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get("/restaurants")
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", new TypeRef<List<RestaurantDetailsDTO>>() {});

        assertThat(restaurantDTOs).hasSize(1);
    }

    @Test
    @Order(5)
    void should_retrieve_single_restaurant() {
        var restaurantDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get("/restaurant/" + this.fixturesController.getRestaurantFixtures().getId())
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", RestaurantDetailsDTO.class);

        assertThat(restaurantDTO).isEqualTo(RestaurantDetailsDTO.from(this.fixturesController.getRestaurantFixtures(), List.of()));
    }

    @Test
    @Order(6)
    void should_update_restaurant_name() {
        CreateRestaurantDTO createRestaurantDTO = new CreateRestaurantDTO();
        createRestaurantDTO.name = "newRestaurantName";
        createRestaurantDTO.location = new CreateLocationDTO();
        createRestaurantDTO.location.address = "242 rue du faubourg Saint-Antoine, 75012 Paris";

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createRestaurantDTO)
        .when()
                .put("/restaurant/" + this.fixturesController.getRestaurantFixtures().getId())
        .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var restaurantDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get(location)
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", RestaurantDetailsDTO.class);

        assertThat(restaurantDTO.name).isEqualTo(createRestaurantDTO.name);
    }

    @Test
    @Order(7)
    void should_not_update_restaurant_name_when_invalid() {
        var updateRestaurantDTO = RestaurantDetailsDTO.from(this.fixturesController.getRestaurantFixtures(), List.of());
        updateRestaurantDTO.name = null;

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateRestaurantDTO)
                .when()
                .put("/restaurant/" + this.fixturesController.getRestaurantFixtures().getId())
                .then()
                .statusCode(400);
    }

    @Test
    @Order(8)
    void should_update_restaurant_location() {
        CreateRestaurantDTO createRestaurantDTO = new CreateRestaurantDTO();
        createRestaurantDTO.name = this.fixturesController.getRestaurantFixtures().getName();
        createRestaurantDTO.location = new CreateLocationDTO();
        createRestaurantDTO.location.address = "1 rue de la paix, 75001 Paris";

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createRestaurantDTO)
                .when()
                .put("/restaurant/" + this.fixturesController.getRestaurantFixtures().getId())
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var restaurantDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", RestaurantDetailsDTO.class);

        assertThat(restaurantDTO.name).isEqualTo(createRestaurantDTO.name);
        assertThat(restaurantDTO.location.latitude).isEqualTo(this.fixturesController.getLocationFixtureFromAddress(createRestaurantDTO.location.address).getLatitude());
        assertThat(restaurantDTO.location.longitude).isEqualTo(this.fixturesController.getLocationFixtureFromAddress(createRestaurantDTO.location.address).getLongitude());
    }

    @Test
    @Order(9)
    void should_not_update_restaurant_location_when_invalid() {
        var updateRestaurantDTO = RestaurantDetailsDTO.from(this.fixturesController.getRestaurantFixtures(), List.of());
        updateRestaurantDTO.location = null;

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateRestaurantDTO)
                .when()
                .put("/restaurant/" + this.fixturesController.getRestaurantFixtures().getId())
                .then()
                .statusCode(400);
    }

    @Test
    @Order(10)
    void should_delete_restaurant() {
        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .when()
                .delete("/restaurant/" + this.fixturesController.getRestaurantFixtures().getId())
                .then()
                .statusCode(200);

        given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get("/restaurant/" + this.fixturesController.getRestaurantFixtures().getId())
                .then()
                .statusCode(404);
    }
}
