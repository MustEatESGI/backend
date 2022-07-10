package fr.esgi.musteat.backend.restaurant.exposition.dto;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.exposition.dto.LocationDTO;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class RestaurantDTO {

    @NotNull
    public Long id;
    @NotNull
    @NotBlank
    public String name;
    @NotNull
    public LocationDTO location;
    @NotNull
    public Long distance;
    @NotNull
    public String imageUrl;

    public RestaurantDTO(Long id, String name, LocationDTO location, Long distance) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.distance = distance;
        this.imageUrl = "http://source.unsplash.com/random?" + name;
    }

    public static RestaurantDTO from(Restaurant restaurant, Location userLocation) {
        return new RestaurantDTO(restaurant.getId(), restaurant.getName(), LocationDTO.from(restaurant.getLocation()), restaurant.getLocation().getDistance(userLocation));
    }

    public static RestaurantDTO from(Restaurant restaurant) {
        return new RestaurantDTO(restaurant.getId(), restaurant.getName(), LocationDTO.from(restaurant.getLocation()), null);
    }

    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDTO that = (RestaurantDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location);
    }
}
