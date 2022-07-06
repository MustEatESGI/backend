package fr.esgi.musteat.backend.search.exposition.controller;

import fr.esgi.musteat.backend.ApiTestBase;
import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import fr.esgi.musteat.backend.search.exposition.dto.MealSearchedDTO;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
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
public class SearchControllerTest extends ApiTestBase {

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
    void should_get_meals() {
        String mealName = fixturesController.getMealFixture().getName();
        var meals = given()
                .headers("Authorization", "Bearer " + this.jwt)
                .get("/search/"+mealName.toLowerCase()+"/price")
                .then()
                .extract()
                .body()
                .jsonPath()
                .getList("", MealSearchedDTO.class);

        assertThat(meals).isNotEmpty();
        assertThat(meals.size()).isEqualTo(1);
        assertThat(meals.get(0).name).isEqualTo(mealName);
    }
}
