package fr.esgi.musteat.backend.location.infrastructure.repository;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.domain.LocationRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InMemoryLocationRepositoryTest implements LocationRepository {

    private final List<Location> locations;

    public InMemoryLocationRepositoryTest() {
        this.locations = new ArrayList<>();
    }

    @Override
    public Optional<Location> get(Long key) {
        if (locations.size() > key) {
            return Optional.of(locations.get(key.intValue()));
        }
        return Optional.empty();
    }

    @Override
    public Long add(Location value) {
        locations.add(value);
        return (long) locations.indexOf(value);
    }

    @Override
    public boolean update(Location value) {
        if (locations.size() > value.getId()) {
            locations.set(value.getId().intValue(), value);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Long key) {
        if (locations.size() > key) {
            locations.remove(key.intValue());
            return true;
        }
        return false;
    }

    @Override
    public List<Location> getAll() {
        return Collections.unmodifiableList(locations);
    }
}
