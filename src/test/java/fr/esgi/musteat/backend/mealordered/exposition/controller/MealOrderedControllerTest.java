package fr.esgi.musteat.backend.mealordered.exposition.controller;

import fr.esgi.musteat.backend.ApiTestBase;
import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import fr.esgi.musteat.backend.mealordered.exposition.dto.CreateMealOrderedDTO;
import fr.esgi.musteat.backend.mealordered.exposition.dto.MealOrderedDTO;
import fr.esgi.musteat.backend.order.exposition.dto.OrderDTO;
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
public class MealOrderedControllerTest extends ApiTestBase {

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
    void should_create_meal_ordered() {
        var createMealOrderedDTO = new CreateMealOrderedDTO();
        createMealOrderedDTO.orderId = this.fixturesController.getOrderFixture().getId();
        createMealOrderedDTO.name = "testMealOrdered";
        createMealOrderedDTO.price = 10L;

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createMealOrderedDTO)
        .when()
                .post("/mealordered")
        .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var mealOrderedDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get(location)
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", MealOrderedDTO.class);

        assertThat(mealOrderedDTO.name).isEqualTo("testMealOrdered");
        assertThat(mealOrderedDTO.price).isEqualTo(10L);
        assertThat(mealOrderedDTO.orderDTO.id).isEqualTo(this.fixturesController.getOrderFixture().getId());
    }

    @Test
    @Order(2)
    void should_not_create_meal_ordered_with_invalid_name() {
        var createMealOrderedDTO = new CreateMealOrderedDTO();
        createMealOrderedDTO.orderId = this.fixturesController.getOrderFixture().getId();
        createMealOrderedDTO.name = null;
        createMealOrderedDTO.price = 10L;

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createMealOrderedDTO)
        .when()
                .post("/mealordered")
        .then()
                .statusCode(400);
    }

    @Test
    @Order(3)
    void should_not_create_meal_ordered_with_invalid_price() {
        var createMealOrderedDTO = new CreateMealOrderedDTO();
        createMealOrderedDTO.orderId = this.fixturesController.getOrderFixture().getId();
        createMealOrderedDTO.name = "testMealOrdered";
        createMealOrderedDTO.price = null;

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createMealOrderedDTO)
        .when()
                .post("/mealordered")
        .then()
                .statusCode(400);
    }

    @Test
    @Order(4)
    void should_not_create_meal_ordered_with_invalid_order_id() {
        var createMealOrderedDTO = new CreateMealOrderedDTO();
        createMealOrderedDTO.orderId = null;
        createMealOrderedDTO.name = "testMealOrdered";
        createMealOrderedDTO.price = 10L;

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createMealOrderedDTO)
        .when()
                .post("/mealordered")
        .then()
                .statusCode(400);
    }

    @Test
    @Order(5)
    void should_retrieve_bootstrapped_ordered_meals() {
        var mealOrderedDTOs = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get("/mealordered")
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getList(".", MealOrderedDTO.class);

        assertThat(mealOrderedDTOs).hasSize(1);
    }

    @Test
    @Order(6)
    void should_retrieve_single_ordered_meal() {
        var mealOrderedDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get("/mealordered/" + this.fixturesController.getMealOrderedFixture().getId())
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", MealOrderedDTO.class);

        assertThat(mealOrderedDTO).isEqualTo(MealOrderedDTO.from(this.fixturesController.getMealOrderedFixture()));
    }

    @Test
    @Order(7)
    void should_update_ordered_meal_name() {
        var updateMealOrderedDTO = new CreateMealOrderedDTO();
        updateMealOrderedDTO.name = "newMealOrderedName";
        updateMealOrderedDTO.price = this.fixturesController.getMealOrderedFixture().getPrice();
        updateMealOrderedDTO.orderId = this.fixturesController.getOrderFixture().getId();

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateMealOrderedDTO)
        .when()
                .put("/mealordered/" + this.fixturesController.getMealOrderedFixture().getId())
        .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var mealOrderedDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get(location)
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", MealOrderedDTO.class);

        assertThat(mealOrderedDTO.name).isEqualTo("newMealOrderedName");
    }

    @Test
    @Order(8)
    void should_not_update_ordered_meal_name_when_invalid() {
        var updateMealOrderedDTO = MealOrderedDTO.from(this.fixturesController.getMealOrderedFixture());
        updateMealOrderedDTO.name = null;

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateMealOrderedDTO)
        .when()
                .put("/mealordered/" + this.fixturesController.getMealOrderedFixture().getId())
        .then()
                .statusCode(400);
    }

    @Test
    @Order(9)
    void should_update_ordered_meal_price() {
        var updateMealOrderedDTO = new CreateMealOrderedDTO();
        updateMealOrderedDTO.name = this.fixturesController.getMealOrderedFixture().getName();
        updateMealOrderedDTO.price = 20L;
        updateMealOrderedDTO.orderId = this.fixturesController.getOrderFixture().getId();

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateMealOrderedDTO)
        .when()
                .put("/mealordered/" + this.fixturesController.getMealOrderedFixture().getId())
        .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var mealOrderedDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get(location)
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", MealOrderedDTO.class);

        assertThat(mealOrderedDTO.price).isEqualTo(20L);
    }

    @Test
    @Order(10)
    void should_not_update_ordered_meal_price_when_invalid() {
        var updateMealOrderedDTO = MealOrderedDTO.from(this.fixturesController.getMealOrderedFixture());
        updateMealOrderedDTO.price = null;

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateMealOrderedDTO)
        .when()
                .put("/mealordered/" + this.fixturesController.getMealOrderedFixture().getId())
        .then()
                .statusCode(400);
    }

    @Test
    @Order(11)
    void should_update_meal_ordered_order() {
        var updateMealOrderedDTO = new CreateMealOrderedDTO();
        this.fixturesController.addOrderFixture();
        updateMealOrderedDTO.name = this.fixturesController.getMealOrderedFixture().getName()  ;
        updateMealOrderedDTO.price = this.fixturesController.getMealOrderedFixture().getPrice();
        updateMealOrderedDTO.orderId = this.fixturesController.getOrderFixture().getId();

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateMealOrderedDTO)
        .when()
                .put("/mealordered/" + this.fixturesController.getMealOrderedFixture().getId())
        .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var mealOrderedDTO =given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get(location)
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", MealOrderedDTO.class);

        assertThat(mealOrderedDTO.orderDTO).isEqualTo(OrderDTO.from(this.fixturesController.getOrderFixture()));
    }

    @Test
    @Order(12)
    void should_not_update_meal_ordered_order_when_invalid() {
        var updateMealOrderedDTO = MealOrderedDTO.from(this.fixturesController.getMealOrderedFixture());
        updateMealOrderedDTO.orderDTO = null;

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(updateMealOrderedDTO)
        .when()
                .put("/mealordered/" + this.fixturesController.getMealOrderedFixture().getId())
        .then()
                .statusCode(400);
    }

    @Test
    @Order(13)
    void should_delete_meal_ordered() {
        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
        .when()
                .delete("/mealordered/" + this.fixturesController.getMealOrderedFixture().getId())
        .then()
                .statusCode(200);

        given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get("/mealordered/" + this.fixturesController.getMealOrderedFixture().getId())
        .then()
                .statusCode(404);
    }
}
