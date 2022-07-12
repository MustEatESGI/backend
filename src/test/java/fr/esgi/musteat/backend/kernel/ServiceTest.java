package fr.esgi.musteat.backend.kernel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public abstract class ServiceTest<R extends Repository<V, K>, V extends Entity<K>, K> {

    protected R repository;
    protected Service<R, V, K> service;

    protected final V value;
    protected final V updatedValue;

    public ServiceTest(V value, V updatedValue) {
        this.value = value;
        this.updatedValue = updatedValue;
    }

    @BeforeEach
    void setup() {
        repository = getRepository();
        service = getService(repository);
    }

    protected abstract R getRepository();
    protected abstract Service<R, V, K> getService(Repository repository);

    @Test
    void should_add_object_to_repository() {
        service.create(value);
        assertThat(repository.getAll()).hasSize(1);
        assertThat(repository.getAll()).isEqualTo(List.of(value));
    }

    @Test
    void should_throw_exception_when_adding_duplicate() {
        service.create(value);
        assertThatThrownBy(() -> service.create(value)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_get_object_from_repository() {
        repository.add(value);
        assertThat(service.get(value.getId())).isEqualTo(value);
    }

    @Test
    void should_be_null_when_getting_non_existing_object() {
        assertThat(service.get(value.getId())).isNull();
    }

    @Test
    void should_update_value() {
        repository.add(value);
        service.update(updatedValue);
        assertThat(repository.get(value.getId()).get()).isEqualTo(updatedValue);
    }

    @Test
    void should_throw_exception_when_updating_non_existing_object() {
        assertThatThrownBy(() -> service.update(updatedValue)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void should_delete_object() {
        repository.add(value);
        service.delete(value.getId());
        assertThat(repository.get(value.getId())).isNotPresent();
    }

    @Test
    void should_throw_exception_when_deleting_non_existing_object() {
        assertThatThrownBy(() -> service.delete(value.getId())).isInstanceOf(EntityNotFoundException.class);
    }
}
