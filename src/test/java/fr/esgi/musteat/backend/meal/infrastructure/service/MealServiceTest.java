package fr.esgi.musteat.backend.meal.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Repository;
import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.ServiceTest;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.domain.MealRepository;
import fr.esgi.musteat.backend.meal.domain.MealValidator;
import fr.esgi.musteat.backend.meal.infrastructure.repository.InMemoryMealRepositoryTest;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealServiceTest extends ServiceTest<MealRepository, Meal, Long> {

    MealRepository mealRepository;
    MealService mealService;

    public MealServiceTest() {
        super(
                new Meal(0L, "test", 10_00L,
                        new Restaurant("name",
                                new Location(10.0, 10.0))),
                new Meal(0L, "test", 15_00L,
                        new Restaurant("name",
                            new Location(10.0, 10.0))));
    }

    @Override
    protected MealRepository getRepository() {
        return new InMemoryMealRepositoryTest();
    }

    @Override
    protected Service<MealRepository, Meal, Long> getService(Repository repository) {
        mealRepository = (MealRepository) repository;
        mealService = new MealService(mealRepository, new MealValidator());
        return mealService;
    }

    @Test
    void should_get_object_by_restaurant_id() {
        Location locationFixture = new Location(0L, 10.0, 10.0);
        Restaurant restaurantFixture = new Restaurant(0L, "test", locationFixture);
        Meal meal = new Meal(0L, "test", 10_00L, restaurantFixture);

        mealRepository.add(meal);
        assertThat(mealService.findByRestaurantId(meal.getRestaurant().getId())).isEqualTo(List.of(meal));
    }

    @Test
    void should_get_object_by_meal_name() {
        mealRepository.add(value);
        assertThat(mealService.findByName(value.getName())).isEqualTo(List.of(value));
    }
}
