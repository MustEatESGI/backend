package fr.esgi.musteat.backend.user.infrastructure.repository;

import fr.esgi.musteat.backend.user.domain.User;
import fr.esgi.musteat.backend.user.domain.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepositoryTest implements UserRepository {

    private final List<User> users;

    public InMemoryUserRepositoryTest() {
        this.users = new ArrayList<>();
    }

    @Override
    public Optional<User> get(Long key) {
        if (users.size() > key) {
            return Optional.of(users.get(key.intValue()));
        }
        return Optional.empty();
    }

    @Override
    public Long add(User value) {
        users.add(value);
        return (long) users.indexOf(value);
    }

    @Override
    public boolean update(User value) {
        if (users.size() > value.getId()) {
            users.set(value.getId().intValue(), value);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Long value) {
        if (users.size() > value) {
            users.remove(value.intValue());
            return true;
        }
        return false;
    }

    @Override
    public List<User> getAll() {
        return Collections.unmodifiableList(users);
    }
}
