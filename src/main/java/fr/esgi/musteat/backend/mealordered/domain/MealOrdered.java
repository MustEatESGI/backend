package fr.esgi.musteat.backend.mealordered.domain;

import fr.esgi.musteat.backend.kernel.Entity;
import fr.esgi.musteat.backend.mealordered.exposition.dto.CreateMealOrderedDTO;
import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;

public class MealOrdered extends Entity<Long> {

    private final String name;
    private final Long price;
    private final Order order;
    private final Restaurant restaurant;

    public MealOrdered(Long id, String name, Long price, Order order, Restaurant restaurant) {
        super(id);
        this.name = name;
        this.price = price;
        this.order = order;
        this.restaurant = restaurant;
    }

    public MealOrdered(String name, Long price, Order order, Restaurant restaurant) {
        super(null);
        this.name = name;
        this.price = price;
        this.order = order;
        this.restaurant = restaurant;
    }

    public static MealOrdered from(CreateMealOrderedDTO createMealOrderedDTO, Order user, Restaurant restaurant) {
        return new MealOrdered(createMealOrderedDTO.name, createMealOrderedDTO.price, user, restaurant);
    }

    public static MealOrdered update(MealOrdered mealOrdered, CreateMealOrderedDTO createMealOrderedDTO) {
        return new MealOrdered(mealOrdered.getId(), createMealOrderedDTO.name, createMealOrderedDTO.price, mealOrdered.getOrder(), mealOrdered.getRestaurant());
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Order getOrder() {
        return order;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public String toString() {
        return "MealOrdered{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", order=" + order +
                ", restaurant=" + restaurant +
                '}';
    }
}
