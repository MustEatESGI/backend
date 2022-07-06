package fr.esgi.musteat.backend.order.exposition.controller;

import fr.esgi.musteat.backend.ApiTestBase;
import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import fr.esgi.musteat.backend.order.exposition.dto.CreateOrderDTO;
import fr.esgi.musteat.backend.order.exposition.dto.OrderDTO;
import fr.esgi.musteat.backend.restaurant.exposition.dto.RestaurantDTO;
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
public class OrderControllerTest extends ApiTestBase {

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
    void should_create_order() {
        var createOrderDTO = new CreateOrderDTO();
        createOrderDTO.userId = this.fixturesController.getUserFixture().getId();
        createOrderDTO.restaurantId = this.fixturesController.getRestaurantFixtures().getId();
        createOrderDTO.mealsId = List.of(this.fixturesController.getMealFixture().getId());

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createOrderDTO)
                .when()
                .post("/order")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var orderDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", OrderDTO.class);

        assertThat(orderDTO).isNotNull();
        assertThat(orderDTO.user).isEqualTo(UserDTO.from(this.fixturesController.getUserFixture()));
        assertThat(orderDTO.restaurant).isEqualTo(RestaurantDTO.from(this.fixturesController.getRestaurantFixtures()));
    }

    @Test
    @Order(2)
    void should_not_create_order_with_invalid_user() {
        var createOrderDTO = new CreateOrderDTO();
        createOrderDTO.userId = null;
        createOrderDTO.restaurantId = this.fixturesController.getRestaurantFixtures().getId();

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createOrderDTO)
                .when()
                .post("/order")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(2)
    void should_not_create_order_with_invalid_restaurant() {
        var createOrderDTO = new CreateOrderDTO();
        createOrderDTO.userId = this.fixturesController.getUserFixture().getId();
        createOrderDTO.restaurantId = null;

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createOrderDTO)
                .when()
                .post("/order")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(3)
    void should_retrieve_bootstrapped_orders() {
        var orderDTOs = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get("/orders")
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", new TypeRef<List<OrderDTO>>() {});

        assertThat(orderDTOs).hasSize(1);
    }

    @Test
    @Order(4)
    void should_retrieve_single_order() {
        var orderDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get("/order/" + this.fixturesController.getOrderFixture().getId())
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", OrderDTO.class);

        assertThat(orderDTO).isEqualTo(OrderDTO.from(this.fixturesController.getOrderFixture()));
    }

    @Test
    @Order(5)
    void should_update_order_user() {
        var createOrderDTO = new  CreateOrderDTO();
        createOrderDTO.userId = this.fixturesController.getUserFixture().getId();
        createOrderDTO.restaurantId = this.fixturesController.getOrderFixture().getRestaurant().getId();
        createOrderDTO.mealsId = List.of(this.fixturesController.getMealFixture().getId());

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createOrderDTO)
        .when()
                .put("/order/" + this.fixturesController.getOrderFixture().getId())
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var orderDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
        .when()
                .get(location)
        .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", OrderDTO.class);

        assertThat(orderDTO.user).isEqualTo(UserDTO.from(this.fixturesController.getOrderFixture().getUser()));
        assertThat(orderDTO.restaurant).isEqualTo(RestaurantDTO.from(this.fixturesController.getOrderFixture().getRestaurant()));
    }

    @Test
    @Order(6)
    void should_not_update_order_user_when_invalid() {
        var createOrderDTO = new  CreateOrderDTO();
        createOrderDTO.userId = null;
        createOrderDTO.restaurantId = this.fixturesController.getOrderFixture().getRestaurant().getId();

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createOrderDTO)
        .when()
                .put("/order/" + this.fixturesController.getOrderFixture().getId())
                .then()
                .statusCode(400);

    }

    @Test
    @Order(7)
    void should_update_order_restaurant() {
        var createOrderDTO = new  CreateOrderDTO();
        createOrderDTO.userId = this.fixturesController.getOrderFixture().getUser().getId();
        createOrderDTO.restaurantId = this.fixturesController.getOrderFixture().getRestaurant().getId();
        createOrderDTO.mealsId = List.of(this.fixturesController.getMealFixture().getId());

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createOrderDTO)
        .when()
                .put("/order/" + this.fixturesController.getOrderFixture().getId())
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var orderDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", OrderDTO.class);

        assertThat(orderDTO.user).isEqualTo(UserDTO.from(this.fixturesController.getOrderFixture().getUser()));
        assertThat(orderDTO.restaurant).isEqualTo(RestaurantDTO.from(this.fixturesController.getOrderFixture().getRestaurant()));
    }

    @Test
    @Order(8)
    void should_not_update_order_restaurant_when_invalid() {
        var createOrderDTO = new  CreateOrderDTO();
        createOrderDTO.userId = this.fixturesController.getOrderFixture().getUser().getId();
        createOrderDTO.restaurantId = null;

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createOrderDTO)
        .when()
                .put("/order/" + this.fixturesController.getOrderFixture().getId())
                .then()
                .statusCode(400);
    }
}
