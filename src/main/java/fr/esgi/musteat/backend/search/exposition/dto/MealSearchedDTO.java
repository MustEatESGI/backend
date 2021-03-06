package fr.esgi.musteat.backend.search.exposition.dto;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.meal.domain.Meal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    public static MealSearchedDTO from(Meal meal, Location userLocation) {
        return new MealSearchedDTO(meal.getId(), meal.getName(), meal.getPrice(), meal.getDistance(userLocation), meal.getRestaurant().getId());
    }

    @Override
    public String toString() {
        return "MealSearchedDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", distance=" + distance +
                ", picture='" + picture + '\'' +
                ", restaurantId=" + restaurantId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealSearchedDTO that = (MealSearchedDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(distance, that.distance) && Objects.equals(picture, that.picture) && Objects.equals(restaurantId, that.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, distance, picture, restaurantId);
    }
}
