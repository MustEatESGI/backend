package fr.esgi.musteat.backend.restaurant.exposition.dto;

import fr.esgi.musteat.backend.location.exposition.dto.LocationDTO;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RestaurantDTO {

    @NotNull
    public Long id;
    @NotNull
    @NotBlank
    public String name;
    @NotNull
    public LocationDTO location;

    public RestaurantDTO(Long id, String name, LocationDTO location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public static RestaurantDTO from(Restaurant restaurant) {
        return new RestaurantDTO(restaurant.getId(), restaurant.getName(), LocationDTO.from(restaurant.getLocation()));
    }
}
