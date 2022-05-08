package fr.esgi.musteat.backend.meal.exposition.dto;

import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.restaurant.exposition.dto.RestaurantDTO;

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
    public RestaurantDTO restaurant;

    public MealDetailsDTO(Long id, String name, Long price, RestaurantDTO restaurant) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
    }

    public static MealDetailsDTO from(Meal meal) {
        return new MealDetailsDTO(meal.getId(), meal.getName(), meal.getPrice(), RestaurantDTO.from(meal.getRestaurant()));
    }
}
