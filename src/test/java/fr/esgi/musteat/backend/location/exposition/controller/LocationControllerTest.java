package fr.esgi.musteat.backend.location.exposition.controller;

import fr.esgi.musteat.backend.ApiTestBase;
import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.exposition.dto.AddressCodingDTO;
import fr.esgi.musteat.backend.location.exposition.dto.CreateLocationDTO;
import fr.esgi.musteat.backend.location.exposition.dto.LocationDTO;
import fr.esgi.musteat.backend.location.infrastructure.service.LocationService;
import fr.esgi.musteat.backend.mealordered.exposition.dto.MealOrderedDTO;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.event.annotation.AfterTestClass;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LocationControllerTest extends ApiTestBase {

    @LocalServerPort
    private int port;

    private String jwt;

    @Autowired
    private FixturesController fixturesController;

    @Mock
    private LocationService locationService;

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
    void should_create_location() {
        var createLocationDTO = new CreateLocationDTO();
        createLocationDTO.address = "242 Rue du Faubourg Saint-Antoine, 75012 Paris";
        Mockito.when(locationService.getLocationFromAddress(createLocationDTO)).thenReturn(new AddressCodingDTO(4.0, 2.0));

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

        var LocationDTO = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getObject(".", LocationDTO.class);

        assertThat(location).isNotNull();
    }
}
