package fr.esgi.musteat.backend.location.infrastructure.repository;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.domain.LocationRepository;
import fr.esgi.musteat.backend.location.infrastructure.database.LocationDB;
import fr.esgi.musteat.backend.location.infrastructure.database.LocationDBRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InDBLocationRepository implements LocationRepository {

    private final LocationDBRepository dbRepository;

    public InDBLocationRepository(LocationDBRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    @Override
    public Optional<Location> get(Long key) {
        return Optional.of(dbRepository.findById(key).orElseThrow().toLocation());
    }

    @Override
    public Long add(Location value) {
        LocationDB locationDB = dbRepository.save(LocationDB.of(value));
        value.setId(locationDB.getId());
        return locationDB.getId();
    }

    @Override
    public boolean update(Location value) {
        if (dbRepository.findById(value.getId()).isPresent()) {
            dbRepository.save(LocationDB.of(value));
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
    public List<Location> getAll() {
        List<Location> locations = new ArrayList<>();
        dbRepository.findAll().forEach(locationDB -> locations.add(locationDB.toLocation()));
        return locations;
    }
}
