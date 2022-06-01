package fr.esgi.musteat.backend.location.exposition.dto;

import javax.validation.constraints.NotNull;

public class CreateLocationDTO {
    @NotNull
    public Double latitude;
    @NotNull
    public Double longitude;
}
