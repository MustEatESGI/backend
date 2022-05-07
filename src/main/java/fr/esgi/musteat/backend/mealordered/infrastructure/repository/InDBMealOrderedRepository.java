package fr.esgi.musteat.backend.mealordered.infrastructure.repository;

import fr.esgi.musteat.backend.mealordered.domain.MealOrdered;
import fr.esgi.musteat.backend.mealordered.domain.MealOrderedRepository;
import fr.esgi.musteat.backend.mealordered.infrastructure.database.MealOrderedDB;
import fr.esgi.musteat.backend.mealordered.infrastructure.database.MealOrderedDBRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InDBMealOrderedRepository implements MealOrderedRepository {

    private final MealOrderedDBRepository dbRepository;

    public InDBMealOrderedRepository(MealOrderedDBRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    @Override
    public Optional<MealOrdered> get(Long key) {
        Optional<MealOrderedDB> mealOrderedDB = dbRepository.findById(key);
        return mealOrderedDB.map(MealOrderedDB::toMealOrdered);
    }

    @Override
    public Long add(MealOrdered value) {
        MealOrderedDB mealOrderedDB = dbRepository.save(MealOrderedDB.fromMealOrdered(value));
        value.setId(mealOrderedDB.getId());
        return mealOrderedDB.getId();
    }

    @Override
    public boolean update(MealOrdered value) {
        if (dbRepository.findById(value.getId()).isEmpty()) {
            return false;
        }
        dbRepository.save(MealOrderedDB.fromMealOrdered(value));
        return true;
    }

    @Override
    public boolean remove(Long value) {
        if (dbRepository.findById(value).isEmpty()) {
            return false;
        }
        dbRepository.delete(dbRepository.findById(value).get());
        return true;
    }

    @Override
    public List<MealOrdered> getAll() {
        List<MealOrdered> mealOrdereds = new ArrayList<>();
        dbRepository.findAll().forEach(mealOrderedDB -> mealOrdereds.add(mealOrderedDB.toMealOrdered()));
        return mealOrdereds;
    }
}
