package fr.esgi.musteat.backend.order.infrastructure.database;

import org.springframework.data.repository.CrudRepository;

public interface OrderDBRepository extends CrudRepository<OrderDB, Long> {
}
