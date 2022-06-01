package fr.esgi.musteat.backend.location.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.domain.LocationRepository;

@org.springframework.stereotype.Service
public class LocationService extends Service<LocationRepository, Location, Long> {

    public LocationService(LocationRepository repository, Validator<Location> validator) {
        super(repository, validator, "location");
    }
}
