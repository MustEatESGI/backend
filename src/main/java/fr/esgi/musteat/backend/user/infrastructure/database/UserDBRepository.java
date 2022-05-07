package fr.esgi.musteat.backend.user.infrastructure.database;

import org.springframework.data.repository.CrudRepository;

public interface UserDBRepository extends CrudRepository<UserDB, Long> {
}
