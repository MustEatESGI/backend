package fr.esgi.musteat.backend.restaurant.exposition.dto;

import fr.esgi.musteat.backend.location.exposition.dto.LocationDTO;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.exposition.dto.MealDTO;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDetailsDTO that = (RestaurantDetailsDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(location, that.location) && Objects.equals(meals, that.meals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, meals);
    }
}
