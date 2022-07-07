package fr.esgi.musteat.backend.mealordered.exposition.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateMealOrderedDTO {
    @NotNull
    @NotBlank
    public String name;
    @NotNull
    public Long price;
    @NotNull
    public Long orderId;
}
