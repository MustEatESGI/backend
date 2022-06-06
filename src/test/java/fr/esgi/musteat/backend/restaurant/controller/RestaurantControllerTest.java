package fr.esgi.musteat.backend.restaurant.controller;

import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.exposition.dto.CreateLocationDTO;
import fr.esgi.musteat.backend.location.exposition.dto.LocationDTO;
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
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestaurantControllerTest {

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
    void should_create_restaurant() {
        var createRestaurantDTO = new CreateRestaurantDTO();
        createRestaurantDTO.name = "testRestaurant";
        createRestaurantDTO.location = new CreateLocationDTO();
        createRestaurantDTO.location.latitude = 10.0;
        createRestaurantDTO.location.longitude = 10.0;

        var location = given()
                .contentType(ContentType.JSON)
                .body(createRestaurantDTO)
        .when()
                .post("/restaurant")
        .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var restaurantDTO = when()
                .get(location)
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", RestaurantDetailsDTO.class);

        assertThat(restaurantDTO).isNotNull();
        assertThat(restaurantDTO.name).isEqualTo("testRestaurant");
        assertThat(restaurantDTO.location.latitude).isEqualTo(10.0);
        assertThat(restaurantDTO.location.longitude).isEqualTo(10.0);
        assertThat(restaurantDTO.meals).isEmpty();
    }

    @Test
    @Order(2)
    void should_not_create_restaurant_with_invalid_name() {
        var createRestaurantDTO = new CreateRestaurantDTO();
        createRestaurantDTO.name = null;
        createRestaurantDTO.location = new CreateLocationDTO();
        createRestaurantDTO.location.latitude = 10.0;
        createRestaurantDTO.location.longitude = 10.0;
        given()
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
        var restaurantDTOs = when()
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
        var restaurantDTO = when()
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
        var updateRestaurantDTO = RestaurantDetailsDTO.from(this.fixturesController.getRestaurantFixtures(), List.of());
        updateRestaurantDTO.name = "newRestaurantName";

        var location = given()
                .contentType(ContentType.JSON)
                .body(updateRestaurantDTO)
        .when()
                .put("/restaurant/" + this.fixturesController.getRestaurantFixtures().getId())
        .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var restaurantDTO = when()
                .get(location)
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", RestaurantDetailsDTO.class);

        assertThat(restaurantDTO).isEqualTo(updateRestaurantDTO);
    }

    @Test
    @Order(7)
    void should_not_update_restaurant_name_when_invalid() {
        var updateRestaurantDTO = RestaurantDetailsDTO.from(this.fixturesController.getRestaurantFixtures(), List.of());
        updateRestaurantDTO.name = null;

        given()
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
        var updateRestaurantDTO = RestaurantDetailsDTO.from(this.fixturesController.getRestaurantFixtures(), List.of());
        updateRestaurantDTO.location = LocationDTO.from(new Location(45.0, 3.0));

        var location = given()
                .contentType(ContentType.JSON)
                .body(updateRestaurantDTO)
                .when()
                .put("/restaurant/" + this.fixturesController.getRestaurantFixtures().getId())
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var restaurantDTO = when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", RestaurantDetailsDTO.class);

        assertThat(restaurantDTO).isEqualTo(updateRestaurantDTO);
    }

    @Test
    @Order(9)
    void should_not_update_restaurant_location_when_invalid() {
        var updateRestaurantDTO = RestaurantDetailsDTO.from(this.fixturesController.getRestaurantFixtures(), List.of());
        updateRestaurantDTO.location = null;

        given()
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
                .when()
                .delete("/restaurant/" + this.fixturesController.getRestaurantFixtures().getId())
                .then()
                .statusCode(200);

        when()
                .get("/restaurant/" + this.fixturesController.getRestaurantFixtures().getId())
                .then()
                .statusCode(404);
    }
}
