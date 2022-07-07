package fr.esgi.musteat.backend.meal.exposition.controller;

import fr.esgi.musteat.backend.ApiTestBase;
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
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MealControllerTest extends ApiTestBase {

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
    void should_create_meal() {
        var createMealDTO = new CreateMealDTO();
        createMealDTO.name = "testMeal";
        createMealDTO.price = 10L;
        createMealDTO.restaurantId = this.fixturesController.getRestaurantFixtures().getId();
        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createMealDTO)
        .when()
                .post("/meal")
        .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var mealDTO = given()
                .header("Authorization", "Bearer " + this.jwt)
        .when()
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
    void should_not_create_meal_with_invalid_name() {
        var createMealDTO = new CreateMealDTO();
        createMealDTO.name = null;
        createMealDTO.price = 10L;
        createMealDTO.restaurantId = this.fixturesController.getRestaurantFixtures().getId();
        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createMealDTO)
                .when()
                .post("/meal")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(3)
    void should_not_create_meal_with_invalid_price() {
        var createMealDTO = new CreateMealDTO();
        createMealDTO.name = "testMeal";
        createMealDTO.price = null;
        createMealDTO.restaurantId = this.fixturesController.getRestaurantFixtures().getId();
        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createMealDTO)
                .when()
                .post("/meal")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(4)
    void should_not_create_meal_with_invalid_restaurant_id() {
        var createMealDTO = new CreateMealDTO();
        createMealDTO.name = "testMeal";
        createMealDTO.price = 10L;
        createMealDTO.restaurantId = null;
        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createMealDTO)
                .when()
                .post("/meal")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(5)
    void should_retrieve_bootstrapped_meals() {
        var mealDTOs = given()
                .header("Authorization", "Bearer " + this.jwt)
        .when()
                .get("/meals")
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", new TypeRef<List<MealDetailsDTO>>() {});

        assertThat(mealDTOs).hasSize(1);
    }

    @Test
    @Order(6)
    void should_retrieve_single_meal() {
        var mealDTO = given()
                .header("Authorization", "Bearer " + this.jwt)
        .when()
                .get("/meal/" + this.fixturesController.getMealFixture().getId())
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", MealDetailsDTO.class);

        assertThat(mealDTO).isEqualTo(MealDetailsDTO.from(this.fixturesController.getMealFixture()));
    }

    @Test
    @Order(7)
    void should_update_meal_name() {
        var updateMealDTO = MealDetailsDTO.from(this.fixturesController.getMealFixture());
        updateMealDTO.name = "newMealName";
        updateMealDTO.picture = "http://source.unsplash.com/random?newMealName";

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateMealDTO)
        .when()
                .put("/meal/" + this.fixturesController.getMealFixture().getId())
        .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var mealDTO = given()
                .header("Authorization", "Bearer " + this.jwt)
        .when()
                .get(location)
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", MealDetailsDTO.class);

        assertThat(mealDTO).isEqualTo(updateMealDTO);
    }

    @Test
    @Order(8)
    void should_not_update_meal_name_when_invalid() {
        var updateMealDTO = MealDetailsDTO.from(this.fixturesController.getMealFixture());
        updateMealDTO.name = null;

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateMealDTO)
                .when()
                .put("/meal/" + this.fixturesController.getMealFixture().getId())
                .then()
                .statusCode(400);
    }

    @Test
    @Order(9)
    void should_update_meal_price() {
        var updateMealDTO = MealDetailsDTO.from(this.fixturesController.getMealFixture());
        updateMealDTO.price = 20L;

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateMealDTO)
                .when()
                .put("/meal/" + this.fixturesController.getMealFixture().getId())
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var mealDTO = given()
                .header("Authorization", "Bearer " + this.jwt)
        .when()
                .get(location)
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", MealDetailsDTO.class);

        assertThat(mealDTO).isEqualTo(updateMealDTO);
    }

    @Test
    @Order(10)
    void should_not_update_meal_price_when_invalid() {
        var updateMealDTO = MealDetailsDTO.from(this.fixturesController.getMealFixture());
        updateMealDTO.price = null;

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateMealDTO)
                .when()
                .put("/meal/" + this.fixturesController.getMealFixture().getId())
                .then()
                .statusCode(400);
    }

    @Test
    @Order(11)
    void should_update_meal_restaurant() {
        var updateMealDTO = MealDetailsDTO.from(this.fixturesController.getMealFixture());
        this.fixturesController.addRestaurantFixtures();
        updateMealDTO.restaurantId = this.fixturesController.getRestaurantFixtures().getId();

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateMealDTO)
                .when()
                .put("/meal/" + this.fixturesController.getMealFixture().getId())
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var mealDTO = given()
                .header("Authorization", "Bearer " + this.jwt)
        .when()
                .get(location)
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", MealDetailsDTO.class);

        assertThat(mealDTO).isEqualTo(updateMealDTO);
    }

    @Test
    @Order(12)
    void should_not_update_meal_restaurant_id_when_invalid() {
        var updateMealDTO = MealDetailsDTO.from(this.fixturesController.getMealFixture());
        updateMealDTO.restaurantId = null;

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateMealDTO)
                .when()
                .put("/meal/" + this.fixturesController.getMealFixture().getId())
                .then()
                .statusCode(400);
    }

    @Test
    @Order(13)
    void should_delete_meal() {
        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .when()
                .delete("/meal/" + this.fixturesController.getMealFixture().getId())
                .then()
                .statusCode(200);

        given()
                .header("Authorization", "Bearer " + this.jwt)
        .when()
                .get("/meal/" + this.fixturesController.getMealFixture().getId())
                .then()
                .statusCode(404);
    }
}
