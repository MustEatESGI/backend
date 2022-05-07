package fr.esgi.musteat.backend.order.exposition.dto;

import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.restaurant.exposition.dto.RestaurantDTO;
import fr.esgi.musteat.backend.user.exposition.dto.UserDTO;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
}
