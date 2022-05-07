package fr.esgi.musteat.backend.location.domain;

import fr.esgi.musteat.backend.kernel.Validator;

public class LocationValidator implements Validator<Location> {
    @Override
    public void validate(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location is null");
        }

        if (location.getLatitude() == null) {
            throw new IllegalArgumentException("Location latitude is null");
        }

        if (location.getLongitude() == null) {
            throw new IllegalArgumentException("Location longitude is null");
        }
    }
}
