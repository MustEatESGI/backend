package fr.esgi.musteat.backend.meal.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealDBRepository extends JpaRepository<MealDB, Long> {

    @Query("SELECT m FROM MealDB m WHERE m.restaurant.id = :restaurantId")
    List<MealDB> findAllByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query("SELECT m FROM MealDB m WHERE SIMILARITY(lower(m.name), lower(:name)) >= 0.3 AND levenshtein(lower(m.name), lower(:name)) <= 16")
    List<MealDB> findAllByName(@Param("name") String name);
}
