package fr.esgi.musteat.backend.kernel;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public abstract class Service<R extends Repository<V, K>, V extends Entity<K>, K> {

    protected final R repository;
    protected final Validator<V> validator;
    protected final String serviceName;

    protected Service(R repository, Validator<V> validator, String serviceName) {
        this.repository = repository;
        this.validator = validator;
        this.serviceName = serviceName;
    }

    public V get(K key) {
        return repository.get(key).orElse(null);
    }

    public List<V> getAll() {
        return repository.getAll();
    }

    public void create(V entity) {
        validator.validate(entity);

        if (entity.getId() != null && repository.get(entity.getId()).isPresent()) {
            throw new IllegalArgumentException(String.format("%s with id %s already exists", serviceName, entity.getId()));
        }

        repository.add(entity);
    }

    public void update(V entity) {
        if (!repository.update(entity)) {
            throw new EntityNotFoundException(String.format("%s with id %s not found", serviceName, entity.getId()));
        }
    }

    public void delete(K key) {
        if (!repository.remove(key)) {
            throw new EntityNotFoundException(String.format("%s with id %s not found", serviceName, key));
        }
    }
}
