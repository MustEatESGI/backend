package fr.esgi.musteat.backend.search.exposition.dto;

import fr.esgi.musteat.backend.meal.domain.Meal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MealSearchedDTO {

    @NotNull
    public Long id;
    @NotNull
    @NotBlank
    public String name;
    @NotNull
    public Long price;
    @NotNull
    public Long distance;
    @NotNull
    @NotBlank
    public String picture;
    @NotNull
    public Long restaurantId;

    public MealSearchedDTO(Long id, String name, Long price, Long distance, Long restaurantId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.distance = distance;
        this.restaurantId = restaurantId;
        this.picture = "http://source.unsplash.com/random?" + name;
    }

    public static MealSearchedDTO from(Meal meal) {
        return new MealSearchedDTO(meal.getId(), meal.getName(), meal.getPrice(), meal.getDistance(), meal.getRestaurant().getId());
    }
}
