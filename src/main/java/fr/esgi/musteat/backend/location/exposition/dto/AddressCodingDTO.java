package fr.esgi.musteat.backend.location.exposition.dto;

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
}
