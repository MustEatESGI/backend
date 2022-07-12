package fr.esgi.musteat.backend.meal.infrastructure.repository;

import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.domain.MealRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryMealRepositoryTest implements MealRepository {

    private final List<Meal> meals;

    public InMemoryMealRepositoryTest() {
        this.meals = new ArrayList<>();
    }


    @Override
    public Optional<Meal> get(Long key) {
        if (meals.size() > key) {
            return Optional.of(meals.get(key.intValue()));
        }
        return Optional.empty();
    }

    @Override
    public Long add(Meal value) {
        meals.add(value);
        return (long) meals.indexOf(value);
    }

    @Override
    public boolean update(Meal value) {
        if (meals.size() > value.getId()) {
            meals.set(value.getId().intValue(), value);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Long value) {
        if (meals.size() > value) {
            meals.remove(value.intValue());
            return true;
        }
        return false;
    }

    @Override
    public List<Meal> getAll() {
        return Collections.unmodifiableList(meals);
    }

    @Override
    public List<Meal> getAllByRestaurantId(Long restaurantId) {
        return meals.stream().filter(meal -> meal.getRestaurant().getId().equals(restaurantId)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Meal> getAllByName(String name) {
        return meals.stream().filter(meal -> meal.getName().equals(name)).collect(Collectors.toUnmodifiableList());
    }
}
