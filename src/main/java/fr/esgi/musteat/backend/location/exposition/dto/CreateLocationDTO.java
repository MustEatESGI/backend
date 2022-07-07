package fr.esgi.musteat.backend.location.exposition.dto;

import javax.validation.constraints.NotNull;

public class CreateLocationDTO {
    @NotNull
    public String address;
}
