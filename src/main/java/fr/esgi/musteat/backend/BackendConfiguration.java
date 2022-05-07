package fr.esgi.musteat.backend;

import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.domain.LocationValidator;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.domain.MealValidator;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.domain.RestaurantValidator;
import fr.esgi.musteat.backend.user.domain.User;
import fr.esgi.musteat.backend.user.domain.UserValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackendConfiguration {

    @Bean
    public Validator<Location> getLocationValidator() {
        return new LocationValidator();
    }

    @Bean
    public Validator<User> getUserValidator() {
        return new UserValidator((LocationValidator) getLocationValidator());
    }

    @Bean
    public Validator<Restaurant> getRestaurantValidator() {
        return new RestaurantValidator();
    }

    @Bean
    public Validator<Meal> getMealValidator() {
        return new MealValidator();
    }
}
