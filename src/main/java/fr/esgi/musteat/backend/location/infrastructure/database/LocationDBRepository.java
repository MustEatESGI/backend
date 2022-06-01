package fr.esgi.musteat.backend.location.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationDBRepository extends JpaRepository<LocationDB, Long> {
}
