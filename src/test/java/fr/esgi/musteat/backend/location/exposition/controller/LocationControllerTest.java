package fr.esgi.musteat.backend.location.exposition.controller;

import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import fr.esgi.musteat.backend.location.exposition.dto.CreateLocationDTO;
import fr.esgi.musteat.backend.location.exposition.dto.LocationDTO;
import fr.esgi.musteat.backend.location.exposition.dto.UpdateLocationDTO;
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
public class LocationControllerTest {

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
    void should_create_location() {
        var createLocationDTO = new CreateLocationDTO();
        createLocationDTO.address = "242 Rue du Faubourg Saint-Antoine, 75012 Paris";
        var location = given()
                .contentType(ContentType.JSON)
                .body(createLocationDTO)
                .when()
                .post("/location")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var locationDTO = when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", LocationDTO.class);

        assertThat(locationDTO).isNotNull();
        assertThat(locationDTO.latitude).isEqualTo(48.849186);
        assertThat(locationDTO.longitude).isEqualTo(2.389684);
    }

    @Test
    @Order(2)
    void should_retrieve_bootstrapped_locations() {
        var locationDTOs = when()
                .get("/locations")
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", new TypeRef<List<LocationDTO>>() {});

        assertThat(locationDTOs).hasSize(1);
    }

    @Test
    @Order(3)
    void should_retrieve_single_location() {
        System.out.println(this.fixturesController.getLocationFixture().getId());
        var locationDTO = when()
                .get("/location/" + this.fixturesController.getLocationFixture().getId())
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", LocationDTO.class);

        assertThat(locationDTO).isEqualTo(LocationDTO.from(this.fixturesController.getLocationFixture()));
    }

    @Test
    @Order(4)
    void should_update_location_name() {
        var updateLocationDTO = new UpdateLocationDTO(
                this.fixturesController.getLocationFixture().getId(),
                "222 Rue du Faubourg Saint-Antoine, 75012 Paris"
        );

        var location = given()
                .contentType(ContentType.JSON)
                .body(updateLocationDTO)
                .when()
                .put("/location/" + this.fixturesController.getLocationFixture().getId())
                .then()
                .statusCode(200)
                .extract()
                .header("Location");

        assertThat(location).isNotEmpty();

        var locationDTO = when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", LocationDTO.class);

        assertThat(locationDTO).isNotNull();
        assertThat(locationDTO.latitude).isEqualTo(48.849186);
        assertThat(locationDTO.longitude).isEqualTo(2.389684);
    }

    @Test
    @Order(5)
    void should_delete_location() {
        given()
                .when()
                .delete("/location/" + this.fixturesController.getLocationFixture().getId())
                .then()
                .statusCode(200);

        when()
                .get("/location/" + this.fixturesController.getLocationFixture().getId())
                .then()
                .statusCode(500);
    }
}
