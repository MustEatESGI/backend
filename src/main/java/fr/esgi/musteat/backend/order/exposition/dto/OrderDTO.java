package fr.esgi.musteat.backend.order.exposition.dto;

import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.restaurant.exposition.dto.RestaurantDTO;
import fr.esgi.musteat.backend.user.exposition.dto.UserDTO;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

public class OrderDTO {

    @NotNull
    public Long id;
    @NotNull
    public LocalDateTime orderDate;
    @NotNull
    public UserDTO user;
    @NotNull
    public RestaurantDTO restaurant;

    public OrderDTO(Long id, LocalDateTime orderDate, UserDTO user, RestaurantDTO restaurant) {
        this.id = id;
        this.orderDate = orderDate;
        this.user = user;
        this.restaurant = restaurant;
    }

    public static OrderDTO from(Order order) {
        return new OrderDTO(order.getId(), order.getOrderDate(), UserDTO.from(order.getUser()), RestaurantDTO.from(order.getRestaurant()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return Objects.equals(id, orderDTO.id) && Objects.equals(orderDate, orderDTO.orderDate) && Objects.equals(user, orderDTO.user) && Objects.equals(restaurant, orderDTO.restaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderDate, user, restaurant);
    }
}
