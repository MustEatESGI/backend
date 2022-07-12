package fr.esgi.musteat.backend.restaurant.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Repository;
import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.ServiceTest;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.domain.RestaurantRepository;
import fr.esgi.musteat.backend.restaurant.domain.RestaurantValidator;
import fr.esgi.musteat.backend.restaurant.infrastructure.repository.InMemoryRestaurantRepositoryTest;

public class RestaurantServiceTest extends ServiceTest<RestaurantRepository, Restaurant, Long> {

    public RestaurantServiceTest() {
        super(
                new Restaurant(0L, "test",
                        new Location(10.0, 10.0)),
                new Restaurant(0L, "new name",
                        new Location(10.0, 10.0)));
    }

    @Override
    protected RestaurantRepository getRepository() {
        return new InMemoryRestaurantRepositoryTest();
    }

    @Override
    protected Service<RestaurantRepository, Restaurant, Long> getService(Repository repository) {
        return new RestaurantService((RestaurantRepository) repository, new RestaurantValidator());
    }
}
