package fr.esgi.musteat.backend.meal.exposition.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateMealDTO {

    @NotNull
    @NotBlank
    public String name;
    @NotNull
    public Long price;
    @NotNull
    public Long restaurantId;
}
