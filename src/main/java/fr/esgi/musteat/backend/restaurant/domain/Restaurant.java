package fr.esgi.musteat.backend.restaurant.domain;

import fr.esgi.musteat.backend.kernel.Entity;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.restaurant.exposition.dto.CreateRestaurantDTO;

import java.util.Objects;

public class Restaurant extends Entity<Long> {

    private final String name;
    private final Location location;

    public Restaurant(Long id, String name, Location location) {
        super(id);
        this.name = name;
        this.location = location;
    }

    public Restaurant(String name, Location location) {
        super(null);
        this.name = name;
        this.location = location;
    }

    public static Restaurant from(CreateRestaurantDTO createRestaurantDTO, Location location) {
        return new Restaurant(createRestaurantDTO.name, location);
    }

    public static Restaurant update(Restaurant restaurant, CreateRestaurantDTO createRestaurantDTO) {
        return new Restaurant(restaurant.getId(), createRestaurantDTO.name, restaurant.getLocation());
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(name, that.name) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, location);
    }
}
