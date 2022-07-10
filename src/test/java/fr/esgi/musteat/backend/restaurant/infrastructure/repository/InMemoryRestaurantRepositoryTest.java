package fr.esgi.musteat.backend.restaurant.infrastructure.repository;

import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.domain.RestaurantRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InMemoryRestaurantRepositoryTest implements RestaurantRepository {

    private final List<Restaurant> restaurants;

    public InMemoryRestaurantRepositoryTest() {
        this.restaurants = new ArrayList<>();
    }

    @Override
    public Optional<Restaurant> get(Long key) {
        if (restaurants.size() > key) {
            return Optional.of(restaurants.get(key.intValue()));
        }
        return Optional.empty();
    }

    @Override
    public Long add(Restaurant value) {
        restaurants.add(value);
        return (long) restaurants.indexOf(value);
    }

    @Override
    public boolean update(Restaurant value) {
        if (restaurants.size() > value.getId()) {
            restaurants.set(value.getId().intValue(), value);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Long value) {
        if (restaurants.size() > value) {
            restaurants.remove(value.intValue());
            return true;
        }
        return false;
    }

    @Override
    public List<Restaurant> getAll() {
        return Collections.unmodifiableList(restaurants);
    }
}
