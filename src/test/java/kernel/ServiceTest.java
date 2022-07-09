package kernel;

import fr.esgi.musteat.backend.kernel.Entity;
import fr.esgi.musteat.backend.kernel.Repository;
import fr.esgi.musteat.backend.kernel.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public abstract class ServiceTest<R extends Repository<V, K>, V extends Entity<K>, K> {

    private R repository;
    private Service<R, V, K> service;

    private final V value;
    private final V updatedValue;

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
        service.create(value);
        assertThat(service.get(value.getId())).isEqualTo(value);
    }

    @Test
    void should_be_null_when_getting_non_existing_object() {
        value.setId(updatedValue.getId());
        assertThat(service.get(value.getId())).isEqualTo(null);
    }

    @Test
    void should_update_value() {
        service.create(value);
        service.update(updatedValue);
        assertThat(repository.get(value.getId()).get()).isEqualTo(updatedValue);
    }

    @Test
    void should_throw_exception_when_updating_non_existing_object() {
        assertThatThrownBy(() -> service.update(updatedValue)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void should_delete_object() {
        service.create(value);
        service.delete(value.getId());
        assertThat(repository.get(value.getId())).isEqualTo(Optional.empty());
    }

    @Test
void should_throw_exception_when_deleting_non_existing_object() {
        assertThatThrownBy(() -> service.delete(value.getId())).isInstanceOf(EntityNotFoundException.class);
    }
}
