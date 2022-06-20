package fr.esgi.musteat.backend;

import fr.esgi.musteat.backend.fixtures.exposition.controller.FixturesController;
import fr.esgi.musteat.backend.location.infrastructure.service.LocationService;
import fr.esgi.musteat.backend.meal.infrastructure.service.MealService;
import fr.esgi.musteat.backend.mealordered.infrastructure.service.MealOrderedService;
import fr.esgi.musteat.backend.order.infrastructure.service.OrderService;
import fr.esgi.musteat.backend.restaurant.infrastructure.service.RestaurantService;
import fr.esgi.musteat.backend.user.infrastructure.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackendConfigurationTest {

    @Bean
    public FixturesController  getFixturesController(
            LocationService locationService,
            MealService mealService,
            MealOrderedService mealOrderedService,
            OrderService orderService,
            RestaurantService restaurantService,
            UserService userService) {
        return new FixturesController(
                locationService,
                mealService,
                mealOrderedService,
                orderService,
                restaurantService,
                userService);
    }
}
