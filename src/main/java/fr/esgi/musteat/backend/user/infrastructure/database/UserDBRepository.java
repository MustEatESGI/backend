package fr.esgi.musteat.backend.user.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDBRepository extends JpaRepository<UserDB, Long> {
}
