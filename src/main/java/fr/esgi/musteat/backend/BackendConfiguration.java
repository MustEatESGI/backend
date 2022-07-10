package fr.esgi.musteat.backend;

import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.domain.LocationValidator;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.domain.MealValidator;
import fr.esgi.musteat.backend.mealordered.domain.MealOrdered;
import fr.esgi.musteat.backend.mealordered.domain.MealOrderedValidator;
import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.order.domain.OrderValidator;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.domain.RestaurantValidator;
import fr.esgi.musteat.backend.user.domain.User;
import fr.esgi.musteat.backend.user.domain.UserValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BackendConfiguration {
    @Bean
    public Validator<Location> getLocationValidator() {
        return new LocationValidator();
    }

    @Bean
    public Validator<User> getUserValidator() {
        return new UserValidator();
    }

    @Bean
    public Validator<Restaurant> getRestaurantValidator() {
        return new RestaurantValidator();
    }

    @Bean
    public Validator<Meal> getMealValidator() {
        return new MealValidator();
    }

    @Bean
    public Validator<Order> getOrderValidator() {
        return new OrderValidator();
    }

    @Bean
    public Validator<MealOrdered> getMealOrderedValidator() {
        return new MealOrderedValidator();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
