package fr.esgi.musteat.backend.meal.infrastructure.database;

import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.restaurant.infrastructure.database.RestaurantDB;

import javax.persistence.*;

@Table(name = "meal")
@Entity
public class MealDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long price;

    @OneToOne
    private RestaurantDB restaurant;

    protected MealDB() {
    }

    public MealDB(Long id, String name, Long price, RestaurantDB restaurant) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
    }

    public static MealDB fromMeal(Meal meal) {
        return new MealDB(meal.getId(), meal.getName(), meal.getPrice(), RestaurantDB.fromRestaurant(meal.getRestaurant()));
    }

    public Meal toMeal() {
        return new Meal(this.id, this.name, this.price, this.restaurant.toRestaurant());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public RestaurantDB getRestaurant() {
        return restaurant;
    }
}
