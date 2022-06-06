package fr.esgi.musteat.backend.meal.exposition.controller;

import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import fr.esgi.musteat.backend.meal.exposition.dto.CreateMealDTO;
import fr.esgi.musteat.backend.meal.exposition.dto.MealDetailsDTO;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class MealControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FixturesController fixturesController;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    void should_create_meal() {
        var createMealDTO = new CreateMealDTO();
        createMealDTO.name = "testMeal";
        createMealDTO.price = 10L;
        createMealDTO.restaurantId = this.fixturesController.getRestaurantFixtures().getId();
        var location = given()
                .contentType(ContentType.JSON)
                .body(createMealDTO)
        .when()
                .post("/meal")
        .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var mealDTO = when()
                .get(location)
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", MealDetailsDTO.class);

        assertThat(mealDTO).isNotNull();
        assertThat(mealDTO.name).isEqualTo("testMeal");
        assertThat(mealDTO.price).isEqualTo(10);
        assertThat(mealDTO.restaurantId).isEqualTo(this.fixturesController.getRestaurantFixtures().getId());
    }
}
