package fr.esgi.musteat.backend.meal.exposition.dto;

import fr.esgi.musteat.backend.meal.domain.Meal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class MealDetailsDTO {

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
    @NotNull
    public Long restaurantId;

    public MealDetailsDTO(Long id, String name, Long price, Long restaurantId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.restaurantId = restaurantId;
        this.picture = "http://source.unsplash.com/random?" + name;
    }

    public static MealDetailsDTO from(Meal meal) {
        return new MealDetailsDTO(meal.getId(), meal.getName(), meal.getPrice(), meal.getRestaurant().getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealDetailsDTO that = (MealDetailsDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(picture, that.picture) && Objects.equals(restaurantId, that.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, picture, restaurantId);
    }
}
