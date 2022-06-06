package fr.esgi.musteat.backend;

import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import fr.esgi.musteat.backend.location.infrastructure.service.LocationService;
import fr.esgi.musteat.backend.restaurant.infrastructure.service.RestaurantService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackendConfigurationTest {

    @Bean
    public FixturesController  getFixturesController(RestaurantService restaurantService, LocationService locationService) {
        return new FixturesController(restaurantService, locationService);
    }
}
