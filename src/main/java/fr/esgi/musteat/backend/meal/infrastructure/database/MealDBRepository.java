package fr.esgi.musteat.backend.meal.infrastructure.database;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MealDBRepository extends CrudRepository<MealDB, Long> {

    @Query("SELECT m FROM MealDB m WHERE m.restaurant.id = :restaurantId")
    List<MealDB> findAllByRestaurantId(@Param("restaurantId") Long restaurantId);
}
