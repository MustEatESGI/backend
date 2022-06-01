package fr.esgi.musteat.backend.restaurant.exposition.dto;

import fr.esgi.musteat.backend.location.exposition.dto.CreateLocationDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateRestaurantDTO {

    @NotNull
    @NotBlank
    public String name;
    @NotNull
    public CreateLocationDTO location;
}
