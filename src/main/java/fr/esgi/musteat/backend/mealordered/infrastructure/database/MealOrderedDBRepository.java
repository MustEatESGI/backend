package fr.esgi.musteat.backend.mealordered.infrastructure.database;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealOrderedDBRepository extends JpaRepository<MealOrderedDB, Long> {
}
