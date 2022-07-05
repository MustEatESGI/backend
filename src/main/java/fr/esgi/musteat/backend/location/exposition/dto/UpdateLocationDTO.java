package fr.esgi.musteat.backend.location.exposition.dto;

import javax.validation.constraints.NotNull;

public class UpdateLocationDTO {
    @NotNull
    public Long id;
    @NotNull
    public String address;

    public UpdateLocationDTO(Long id, String address) {
        this.id = id;
        this.address = address;
    }
}
