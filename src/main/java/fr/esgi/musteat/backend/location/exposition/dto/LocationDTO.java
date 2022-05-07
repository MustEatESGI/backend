package fr.esgi.musteat.backend.location.exposition.dto;

import fr.esgi.musteat.backend.location.domain.Location;

import javax.validation.constraints.NotNull;

public class LocationDTO {

    @NotNull
    public Double latitude;

    @NotNull
    public Double longitude;

    public LocationDTO(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LocationDTO from(Location location) {
        return new LocationDTO(location.getLatitude(), location.getLongitude());
    }
}
