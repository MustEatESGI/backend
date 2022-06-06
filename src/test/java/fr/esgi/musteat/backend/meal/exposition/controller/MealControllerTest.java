package fr.esgi.musteat.backend.meal.exposition.controller;

import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import fr.esgi.musteat.backend.meal.exposition.dto.CreateMealDTO;
import fr.esgi.musteat.backend.meal.exposition.dto.MealDetailsDTO;
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

    @AfterTestClass
    public void tearDown() {
        fixturesController.resetFixtures();
    }

    @Test
    @Order(1)
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

    @Test
    @Order(2)
    void should_retrieve_bootstrapped_meals() {
        var mealDTOs = when()
                .get("/meals")
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", new TypeRef<List<MealDetailsDTO>>() {});

        assertThat(mealDTOs).hasSize(1);
    }

    @Test
    @Order(3)
    void should_retrieve_single_meal() {
        var mealDTO = when()
                .get("/meal/" + this.fixturesController.getMealFixture().getId())
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", MealDetailsDTO.class);

        assertThat(mealDTO).isEqualTo(MealDetailsDTO.from(this.fixturesController.getMealFixture()));
    }

    @Test
    @Order(4)
    void should_update_meal_name() {
        var updateMealDTO = MealDetailsDTO.from(this.fixturesController.getMealFixture());
        updateMealDTO.name = "newMealName";
        updateMealDTO.picture = "http://source.unsplash.com/random?newMealName";

        var location = given()
                .contentType(ContentType.JSON)
                .body(updateMealDTO)
        .when()
                .put("/meal/" + this.fixturesController.getMealFixture().getId())
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

        assertThat(mealDTO).isEqualTo(updateMealDTO);
    }

    @Test
    @Order(5)
    void should_update_meal_price() {
        var updateMealDTO = MealDetailsDTO.from(this.fixturesController.getMealFixture());
        updateMealDTO.price = 20L;

        var location = given()
                .contentType(ContentType.JSON)
                .body(updateMealDTO)
                .when()
                .put("/meal/" + this.fixturesController.getMealFixture().getId())
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

        assertThat(mealDTO).isEqualTo(updateMealDTO);
    }

    @Test
    @Order(6)
    void should_update_meal_restaurant() {
        var updateMealDTO = MealDetailsDTO.from(this.fixturesController.getMealFixture());
        this.fixturesController.addRestaurantFixtures();
        updateMealDTO.restaurantId = this.fixturesController.getRestaurantFixtures().getId();

        var location = given()
                .contentType(ContentType.JSON)
                .body(updateMealDTO)
                .when()
                .put("/meal/" + this.fixturesController.getMealFixture().getId())
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

        assertThat(mealDTO).isEqualTo(updateMealDTO);
    }

    @Test
    @Order(7)
    void should_delete_meal() {
        given()
                .when()
                .delete("/meal/" + this.fixturesController.getMealFixture().getId())
                .then()
                .statusCode(200);

        when()
                .get("/meal/" + this.fixturesController.getMealFixture().getId())
                .then()
                .statusCode(404);
    }
}
