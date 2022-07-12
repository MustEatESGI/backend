package fr.esgi.musteat.backend.mealordered.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Repository;
import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.ServiceTest;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.mealordered.domain.MealOrdered;
import fr.esgi.musteat.backend.mealordered.domain.MealOrderedRepository;
import fr.esgi.musteat.backend.mealordered.domain.MealOrderedValidator;
import fr.esgi.musteat.backend.mealordered.infrastructure.repository.InMemoryMealOrderedRepositoryTest;
import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.user.domain.User;

import java.time.LocalDateTime;

public class MealOrderedServiceTest extends ServiceTest<MealOrderedRepository, MealOrdered, Long> {

    public MealOrderedServiceTest() {
        super(
                new MealOrdered(0L, "test", 10_00L,
                        new Order(LocalDateTime.now(),
                                new User("name", "Password1",
                                        new Location(10.0, 10.0)),
                                new Restaurant("restaurant",
                                        new Location(10.0, 10.0)))),
                new MealOrdered(0L, "test", 15_00L,
                        new Order(LocalDateTime.now(),
                                new User("name", "Password1",
                                        new Location(10.0, 10.0)),
                                new Restaurant("restaurant",
                                        new Location(10.0, 10.0)))));
    }

    @Override
    protected MealOrderedRepository getRepository() {
        return new InMemoryMealOrderedRepositoryTest();
    }

    @Override
    protected Service<MealOrderedRepository, MealOrdered, Long> getService(Repository repository) {
        return new MealOrderedService((MealOrderedRepository) repository, new MealOrderedValidator());
    }
}
