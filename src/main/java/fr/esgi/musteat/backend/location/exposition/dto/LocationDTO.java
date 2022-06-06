package fr.esgi.musteat.backend.location.exposition.dto;

import fr.esgi.musteat.backend.location.domain.Location;

import javax.validation.constraints.NotNull;

public class LocationDTO {

    @NotNull
    public Long id;
    @NotNull
    public Double latitude;

    @NotNull
    public Double longitude;

    public LocationDTO(Long id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LocationDTO from(Location location) {
        return new LocationDTO(location.getId(), location.getLatitude(), location.getLongitude());
    }
}
