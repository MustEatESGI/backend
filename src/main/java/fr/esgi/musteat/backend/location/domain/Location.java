package fr.esgi.musteat.backend.location.domain;

import fr.esgi.musteat.backend.kernel.Entity;
import fr.esgi.musteat.backend.location.exposition.dto.CreateLocationDTO;

public class Location extends Entity<Long> {

    private final Double latitude;
    private final Double longitude;

    public Location(Long id, Double latitude, Double longitude) {
        super(id);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(Double latitude, Double longitude) {
        super(null);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Location from(CreateLocationDTO createLocationDTO) {
        return new Location(createLocationDTO.latitude, createLocationDTO.longitude);
    }

    public static Location update(Long id, CreateLocationDTO createLocationDTO) {
        return new Location(id, createLocationDTO.latitude, createLocationDTO.longitude);
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
