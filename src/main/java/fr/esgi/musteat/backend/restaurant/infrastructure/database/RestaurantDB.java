package fr.esgi.musteat.backend.restaurant.infrastructure.database;

import fr.esgi.musteat.backend.location.infrastructure.database.LocationDB;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;

import javax.persistence.*;

@Table(name = "restaurant")
@Entity
public class RestaurantDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    private LocationDB location;

    protected RestaurantDB() {
    }

    public RestaurantDB(Long id, String name, LocationDB location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public static RestaurantDB fromRestaurant(Restaurant restaurant) {
        return new RestaurantDB(restaurant.getId(), restaurant.getName(), LocationDB.fromLocation(restaurant.getLocation()));
    }

    public Restaurant toRestaurant() {
        return new Restaurant(id, name, location.toLocation());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocationDB getLocation() {
        return location;
    }
}
