package fr.esgi.musteat.backend.location.exposition.dto;

import fr.esgi.musteat.backend.location.domain.Location;

import javax.validation.constraints.NotNull;

public class AddressCodingDTO {

    @NotNull
    public Double latitude;

    @NotNull
    public Double longitude;

    public AddressCodingDTO(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static AddressCodingDTO from(Location location) {
        return new AddressCodingDTO(location.getLatitude(), location.getLongitude());
    }
}
