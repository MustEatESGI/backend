package fr.esgi.musteat.backend.meal.exposition.dto;

import fr.esgi.musteat.backend.meal.domain.Meal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MealDetailsDTO {

    @NotNull
    public Long id;
    @NotNull
    @NotBlank
    public String name;
    @NotNull
    public Long price;
    @NotNull
    public Long restaurantId;

    public MealDetailsDTO(Long id, String name, Long price, Long restaurantId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.restaurantId = restaurantId;
    }

    public static MealDetailsDTO from(Meal meal) {
        return new MealDetailsDTO(meal.getId(), meal.getName(), meal.getPrice(), meal.getRestaurant().getId());
    }
}
