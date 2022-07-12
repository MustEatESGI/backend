package fr.esgi.musteat.backend.mealordered.infrastructure.repository;

import fr.esgi.musteat.backend.mealordered.domain.MealOrdered;
import fr.esgi.musteat.backend.mealordered.domain.MealOrderedRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InMemoryMealOrderedRepositoryTest implements MealOrderedRepository {

    private final List<MealOrdered> mealsOrdered;

    public InMemoryMealOrderedRepositoryTest() {
        this.mealsOrdered = new ArrayList<>();
    }

    @Override
    public Optional<MealOrdered> get(Long key) {
        if (mealsOrdered.size() > key) {
            return Optional.of(mealsOrdered.get(key.intValue()));
        }
        return Optional.empty();
    }

    @Override
    public Long add(MealOrdered value) {
        mealsOrdered.add(value);
        return (long) mealsOrdered.indexOf(value);
    }

    @Override
    public boolean update(MealOrdered value) {
        if (mealsOrdered.size() > value.getId()) {
            mealsOrdered.set(value.getId().intValue(), value);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Long value) {
        if (mealsOrdered.size() > value) {
            mealsOrdered.remove(value.intValue());
            return true;
        }
        return false;
    }

    @Override
    public List<MealOrdered> getAll() {
        return Collections.unmodifiableList(mealsOrdered);
    }
}
