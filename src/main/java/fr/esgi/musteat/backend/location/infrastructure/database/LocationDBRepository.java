package fr.esgi.musteat.backend.location.infrastructure.database;

import org.springframework.data.repository.CrudRepository;

public interface LocationDBRepository extends CrudRepository<LocationDB, Long> {
}
