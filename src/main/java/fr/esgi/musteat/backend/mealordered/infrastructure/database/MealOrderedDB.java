package fr.esgi.musteat.backend.mealordered.infrastructure.database;

import fr.esgi.musteat.backend.mealordered.domain.MealOrdered;
import fr.esgi.musteat.backend.order.infrastructure.database.OrderDB;

import javax.persistence.*;

@Table(name = "meal_ordered")
@Entity
public class MealOrderedDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long price;

    @OneToOne
    private OrderDB order;

    protected MealOrderedDB() {
    }

    public MealOrderedDB(Long id, String name, Long price, OrderDB order) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.order = order;
    }

    public static MealOrderedDB fromMealOrdered(MealOrdered mealOrdered) {
        return new MealOrderedDB(mealOrdered.getId(), mealOrdered.getName(), mealOrdered.getPrice(), OrderDB.fromOrder(mealOrdered.getOrder()));
    }

    public MealOrdered toMealOrdered() {
        return new MealOrdered(this.id, this.name, this.price, this.order.toOrder());
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

    public OrderDB getOrder() {
        return order;
    }
}
