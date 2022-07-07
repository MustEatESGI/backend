package fr.esgi.musteat.backend.user.infrastructure.repository;

import fr.esgi.musteat.backend.user.domain.User;
import fr.esgi.musteat.backend.user.domain.UserRepository;
import fr.esgi.musteat.backend.user.infrastructure.database.UserDB;
import fr.esgi.musteat.backend.user.infrastructure.database.UserDBRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InDBUserRepository implements UserRepository {

    private final UserDBRepository dbRepository;

    public InDBUserRepository(UserDBRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    @Override
    public Optional<User> get(Long key) {
        Optional<UserDB> userDB = dbRepository.findById(key);
        return userDB.map(UserDB::toUser);
    }

    @Override
    public Long add(User value) {
        UserDB userDB = dbRepository.save(UserDB.fromUser(value));
        value.setId(userDB.getId());
        return userDB.getId();
    }

    @Override
    public boolean update(User value) {
        if (dbRepository.findById(value.getId()).isPresent()) {
            dbRepository.save(UserDB.fromUser(value));
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Long value) {
        if (dbRepository.findById(value).isPresent()) {
            dbRepository.delete(dbRepository.findById(value).get());
            return true;
        }
        return false;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        dbRepository.findAll().forEach(userDB -> users.add(userDB.toUser()));
        return users;
    }
}
