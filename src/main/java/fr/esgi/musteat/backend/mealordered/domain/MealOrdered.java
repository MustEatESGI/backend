package fr.esgi.musteat.backend.mealordered.domain;

import fr.esgi.musteat.backend.kernel.Entity;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.mealordered.exposition.dto.CreateMealOrderedDTO;
import fr.esgi.musteat.backend.order.domain.Order;
import org.aspectj.weaver.ast.Or;

public class MealOrdered extends Entity<Long> {

    private final String name;
    private final Long price;
    private final Order order;

    public MealOrdered(Long id, String name, Long price, Order order) {
        super(id);
        this.name = name;
        this.price = price;
        this.order = order;
    }

    public MealOrdered(String name, Long price, Order order) {
        super(null);
        this.name = name;
        this.price = price;
        this.order = order;
    }

    public static MealOrdered from(CreateMealOrderedDTO createMealOrderedDTO, Order order) {
        return new MealOrdered(createMealOrderedDTO.name, createMealOrderedDTO.price, order);
    }

    public static MealOrdered from(Meal meal, Order order) {
        return new MealOrdered(meal.getName(), meal.getPrice(), order);
    }

    public static MealOrdered update(MealOrdered mealOrdered, CreateMealOrderedDTO createMealOrderedDTO, Order order) {
        return new MealOrdered(mealOrdered.getId(), createMealOrderedDTO.name, createMealOrderedDTO.price, order);
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

    @Override
    public String toString() {
        return "MealOrdered{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", order=" + order +
                '}';
    }
}
