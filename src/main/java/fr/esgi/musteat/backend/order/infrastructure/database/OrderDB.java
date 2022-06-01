package fr.esgi.musteat.backend.order.infrastructure.database;

import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.restaurant.infrastructure.database.RestaurantDB;
import fr.esgi.musteat.backend.user.infrastructure.database.UserDB;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "\"order\"")
@Entity
public class OrderDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;

    @OneToOne
    private UserDB user;

    @OneToOne
    private RestaurantDB restaurant;

    protected OrderDB() {
    }

    public OrderDB(Long id, LocalDateTime orderDate, UserDB user, RestaurantDB restaurant) {
        this.id = id;
        this.orderDate = orderDate;
        this.user = user;
        this.restaurant = restaurant;
    }

    public static OrderDB fromOrder(Order order) {
        return new OrderDB(order.getId(), order.getOrderDate(), UserDB.fromUser(order.getUser()), RestaurantDB.fromRestaurant(order.getRestaurant()));
    }

    public Order toOrder() {
        return new Order(this.id, this.orderDate, this.user.toUser(), this.restaurant.toRestaurant());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public UserDB getUser() {
        return user;
    }

    public RestaurantDB getRestaurant() {
        return restaurant;
    }
}
