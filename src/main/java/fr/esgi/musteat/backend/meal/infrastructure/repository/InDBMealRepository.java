package fr.esgi.musteat.backend.meal.infrastructure.repository;

import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.domain.MealRepository;
import fr.esgi.musteat.backend.meal.infrastructure.database.MealDB;
import fr.esgi.musteat.backend.meal.infrastructure.database.MealDBRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InDBMealRepository implements MealRepository {

    private final MealDBRepository dbRepository;

    public InDBMealRepository(MealDBRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    @Override
    public List<Meal> getAllByRestaurantId(Long restaurantId) {
        List<Meal> meals = new ArrayList<>();
        dbRepository.findAllByRestaurantId(restaurantId).forEach(mealDB -> meals.add(mealDB.toMeal()));
        return meals;
    }

    @Override
    public List<Meal> getAllByName(String name) {
        List<Meal> meals = new ArrayList<>();
        dbRepository.findAllByName(name).forEach(mealDB -> meals.add(mealDB.toMeal()));
        return meals;
    }

    @Override
    public Optional<Meal> get(Long key) {
        Optional<MealDB> mealDB = dbRepository.findById(key);
        return mealDB.map(MealDB::toMeal);
    }

    @Override
    public Long add(Meal value) {
        MealDB mealDB = dbRepository.save(MealDB.fromMeal(value));
        value.setId(mealDB.getId());
        return mealDB.getId();
    }

    @Override
    public boolean update(Meal value) {
        if (dbRepository.findById(value.getId()).isPresent()) {
            dbRepository.save(MealDB.fromMeal(value));
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Long value) {
        if (dbRepository.findById(value).isPresent()) {
            dbRepository.delete(dbRepository.findById(value).get());
            return true;
        }
        return false;
    }

    @Override
    public List<Meal> getAll() {
        List<Meal> meals = new ArrayList<>();
        dbRepository.findAll().forEach(mealDB -> meals.add(mealDB.toMeal()));
        return meals;
    }
}
