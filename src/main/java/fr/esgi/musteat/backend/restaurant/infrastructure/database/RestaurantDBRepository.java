package fr.esgi.musteat.backend.restaurant.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantDBRepository extends JpaRepository<RestaurantDB, Long> {
}
