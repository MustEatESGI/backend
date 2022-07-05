package fr.esgi.musteat.backend.meal.domain;

import fr.esgi.musteat.backend.kernel.Entity;
import fr.esgi.musteat.backend.meal.exposition.dto.CreateMealDTO;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;

import java.util.Objects;

public class Meal extends Entity<Long> {

    private final String name;
    private final Long price;
    private final Restaurant restaurant;

    public Meal(Long id, String name, Long price, Restaurant restaurant) {
        super(id);
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
    }

    public Meal(String name, Long price, Restaurant restaurant) {
        super(null);
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
    }

    public static Meal from(CreateMealDTO createMealDTO, Restaurant restaurant) {
        return new Meal(createMealDTO.name, createMealDTO.price, restaurant);
    }

    public static Meal update(Meal meal, CreateMealDTO createMealDTO, Restaurant restaurant) {
        return new Meal(meal.getId(), createMealDTO.name, createMealDTO.price, restaurant);
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", restaurant=" + restaurant +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Meal meal = (Meal) o;
        return Objects.equals(name, meal.name) && Objects.equals(price, meal.price) && Objects.equals(restaurant, meal.restaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, price, restaurant);
    }
}
