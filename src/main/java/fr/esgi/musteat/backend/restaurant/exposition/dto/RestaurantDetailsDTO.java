package fr.esgi.musteat.backend.restaurant.exposition.dto;

import fr.esgi.musteat.backend.location.exposition.dto.LocationDTO;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.exposition.dto.MealDTO;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantDetailsDTO {

    @NotNull
    public Long id;
    @NotNull
    @NotBlank
    public String name;
    @NotNull
    public LocationDTO location;
    @NotNull
    public List<MealDTO> meals;

    public RestaurantDetailsDTO(Long id, String name, LocationDTO location, List<MealDTO> meals) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.meals = meals;
    }

    public static RestaurantDetailsDTO from(Restaurant restaurant, List<Meal> meals) {
        return new RestaurantDetailsDTO(restaurant.getId(), restaurant.getName(), LocationDTO.from(restaurant.getLocation()), meals.stream().map(MealDTO::from).collect(Collectors.toList()));
    }
}
