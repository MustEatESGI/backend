package fr.esgi.musteat.backend.location.exposition.controller;

import fr.esgi.musteat.backend.ApiTestBase;
import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import fr.esgi.musteat.backend.location.exposition.dto.CreateLocationDTO;
import fr.esgi.musteat.backend.location.exposition.dto.LocationDTO;
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
class LocationControllerTest extends ApiTestBase {

    @LocalServerPort
    private int port;

    private String jwt;

    @Autowired
    private FixturesController fixturesController;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        jwt = this.getToken(fixturesController.getUserFixture().getName(), fixturesController.getUserFixture().getPassword());
    }

    @AfterTestClass
    public void tearDown() {
        fixturesController.resetFixtures();
    }

    @Test
    @Order(1)
    void should_retrieve_single_location() {
        var locationDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .when()
                .get("/location/" + fixturesController.getLocationFixture().getId())
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", LocationDTO.class);

        assertThat(locationDTO.latitude).isEqualTo(fixturesController.getLocationFixture().getLatitude());
        assertThat(locationDTO.longitude).isEqualTo(fixturesController.getLocationFixture().getLongitude());
    }

    @Test
    @Order(2)
    void should_be_not_found_when_retrieving_unknown_location() {
        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .when()
                .get("/location/" + -1)
                .then()
                .statusCode(404);
    }

    @Test
    @Order(3)
    void should_retrieve_locations() {
        var locationDTOs = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .when()
                .get("/locations")
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getList(".", LocationDTO.class);

        assertThat(locationDTOs).hasSize(1);
    }

    @Test
    @Order(4)
    void should_create_location() {
        var createLocationDTO = new CreateLocationDTO();
        createLocationDTO.address = "242 Rue du Faubourg Saint-Antoine, 75012 Paris";

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createLocationDTO)
                .when()
                .post("/location")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var locationDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", LocationDTO.class);

        assertThat(locationDTO.latitude).isEqualTo(48.849186);
        assertThat(locationDTO.longitude).isEqualTo(2.389684);
    }

    @Test
    @Order(5)
    void should_not_create_location_when_the_address_is_wrong_or_missing() {
        var createLocationDTO = new CreateLocationDTO();

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createLocationDTO)
                .when()
                .post("/location")
                .then()
                .statusCode(400)
                .extract()
                .header("Location");
    }

    @Test
    @Order(6)
    void should_update_location() {
        var createLocationDTO = new CreateLocationDTO();
        createLocationDTO.address = "243 Rue du Faubourg Saint-Antoine, 75012 Paris";

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createLocationDTO)
                .when()
                .put("/location/" + this.fixturesController.getLocationFixture().getId())
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var locationDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", LocationDTO.class);

        assertThat(locationDTO.latitude).isEqualTo(48.85011);
        assertThat(locationDTO.longitude).isEqualTo(2.385364);
    }

    @Test
    @Order(7)
    void should_not_update_location_when_the_address_is_wrong_or_missing() {
        var createLocationDTO = new CreateLocationDTO();

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createLocationDTO)
                .when()
                .put("/location/" + this.fixturesController.getLocationFixture().getId())
                .then()
                .statusCode(400)
                .extract()
                .header("Location");
    }

    @Test
    @Order(8)
    void should_not_update_location_when_the_location_id_does_not_exist() {
        var createLocationDTO = new CreateLocationDTO();

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createLocationDTO)
                .when()
                .put("/location/" + -1)
                .then()
                .statusCode(400)
                .extract()
                .header("Location");
    }

    @Test
    @Order(9)
    void should_not_delete_location_when_the_location_id_does_not_exist() {
        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .when()
                .delete("/location/" + -1)
                .then()
                .statusCode(400);
    }

    @Test
    @Order(10)
    void should_delete_location() {
        var createLocationDTO = new CreateLocationDTO();
        createLocationDTO.address = "242 Rue du Faubourg Saint-Antoine, 75012 Paris";

        var location = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .body(createLocationDTO)
                .when()
                .post("/location")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        var locationId = location.substring(location.lastIndexOf("/") + 1);

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .contentType(ContentType.JSON)
                .when()
                .delete("/location/" + locationId)
                .then()
                .statusCode(200);

        given()
                .headers("Authorization", "Bearer " + this.jwt)
                .when()
                .get("/mealordered/" + locationId)
                .then()
                .statusCode(404);
    }
}
