package fr.esgi.musteat.backend.order.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDBRepository extends JpaRepository<OrderDB, Long> {
}
