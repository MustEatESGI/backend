package fr.esgi.musteat.backend.location.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Repository;
import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.ServiceTest;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.domain.LocationRepository;
import fr.esgi.musteat.backend.location.domain.LocationValidator;
import fr.esgi.musteat.backend.location.infrastructure.repository.InMemoryLocationRepositoryTest;

public class LocationServiceTest extends ServiceTest<LocationRepository, Location, Long> {

    public LocationServiceTest() {
        super(new Location(0L, 10.0, 10.0), new Location(0L, 15.0, 15.0));
    }

    @Override
    protected LocationRepository getRepository() {
        return new InMemoryLocationRepositoryTest();
    }

    @Override
    protected Service<LocationRepository, Location, Long> getService(Repository repository) {
        return new LocationService((LocationRepository) repository, new LocationValidator());
    }
}
