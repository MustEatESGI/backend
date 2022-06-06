package fr.esgi.musteat.backend.meal.exposition.dto;

import fr.esgi.musteat.backend.meal.domain.Meal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "MealDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", picture='" + picture + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealDTO mealDTO = (MealDTO) o;
        return Objects.equals(id, mealDTO.id) && Objects.equals(name, mealDTO.name) && Objects.equals(price, mealDTO.price) && Objects.equals(picture, mealDTO.picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, picture);
    }
}
