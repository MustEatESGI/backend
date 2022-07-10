package fr.esgi.musteat.backend.mealordered.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Repository;
import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.ServiceTest;
import fr.esgi.musteat.backend.mealordered.domain.MealOrdered;
import fr.esgi.musteat.backend.mealordered.domain.MealOrderedRepository;
import fr.esgi.musteat.backend.mealordered.domain.MealOrderedValidator;
import fr.esgi.musteat.backend.mealordered.infrastructure.repository.InMemoryMealOrderedRepositoryTest;

public class MealOrderedServiceTest extends ServiceTest<MealOrderedRepository, MealOrdered, Long> {

    public MealOrderedServiceTest() {
        super(new MealOrdered(0L, "test", 10_00L, null), new MealOrdered(0L, "test", 15_00L, null));
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
