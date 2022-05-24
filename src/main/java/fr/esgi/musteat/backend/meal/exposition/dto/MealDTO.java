package fr.esgi.musteat.backend.meal.exposition.dto;

import fr.esgi.musteat.backend.meal.domain.Meal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MealDTO {
    @NotNull
    public Long id;
    @NotNull
    @NotBlank
    public String name;
    @NotNull
    public Long price;
    @NotNull
    @NotBlank
    public String picture;

    public MealDTO(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.picture = "http://source.unsplash.com/random?" + name;
    }

    public static MealDTO from(Meal meal) {
        return new MealDTO(meal.getId(), meal.getName(), meal.getPrice());
    }
}
