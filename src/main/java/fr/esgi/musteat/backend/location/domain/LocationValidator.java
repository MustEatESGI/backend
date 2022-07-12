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

        if (location.getLatitude() > 90 || location.getLatitude() < -90) {
            throw new IllegalArgumentException("Location latitude is out of range");
        }

        if (location.getLongitude() == null) {
            throw new IllegalArgumentException("Location longitude is null");
        }

        if (location.getLongitude() > 180 || location.getLongitude() < -180) {
            throw new IllegalArgumentException("Location longitude is out of range");
        }
    }
}
