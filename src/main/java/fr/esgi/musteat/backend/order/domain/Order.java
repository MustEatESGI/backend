package fr.esgi.musteat.backend.order.domain;

import fr.esgi.musteat.backend.kernel.Entity;
import fr.esgi.musteat.backend.order.exposition.dto.CreateOrderDTO;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.user.domain.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order extends Entity<Long> {

    private final LocalDateTime orderDate;
    private final User user;
    private final Restaurant restaurant;

    public Order(Long id, LocalDateTime orderDate, User user, Restaurant restaurant) {
        super(id);
        this.orderDate = orderDate;
        this.user = user;
        this.restaurant = restaurant;
    }

    public Order(LocalDateTime orderDate, User user, Restaurant restaurant) {
        super(null);
        this.orderDate = orderDate;
        this.user = user;
        this.restaurant = restaurant;
    }

    public static Order from(CreateOrderDTO createOrderDTO, User user, Restaurant restaurant) {
        return new Order(createOrderDTO.orderDate, user, restaurant);
    }

    public static Order update(Order order, CreateOrderDTO createOrderDTO) {
        return new Order(order.getId(), createOrderDTO.orderDate, order.getUser(), order.getRestaurant());
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public User getUser() {
        return user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", user=" + user +
                ", restaurant=" + restaurant +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return Objects.equals(orderDate, order.orderDate) && Objects.equals(user, order.user) && Objects.equals(restaurant, order.restaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orderDate, user, restaurant);
    }
}
