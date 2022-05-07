package fr.esgi.musteat.backend.mealordered.exposition.controller;

import fr.esgi.musteat.backend.mealordered.domain.MealOrdered;
import fr.esgi.musteat.backend.mealordered.exposition.dto.CreateMealOrderedDTO;
import fr.esgi.musteat.backend.mealordered.exposition.dto.MealOrderedDTO;
import fr.esgi.musteat.backend.mealordered.infrastructure.service.MealOrderedService;
import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.order.infrastructure.service.OrderService;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.infrastructure.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class MealOrderedController {

    private final MealOrderedService mealOrderedService;

    private final RestaurantService restaurantService;

    private final OrderService orderService;

    public MealOrderedController(MealOrderedService mealOrderedService, RestaurantService restaurantService, OrderService orderService) {
        this.mealOrderedService = mealOrderedService;
        this.restaurantService = restaurantService;
        this.orderService = orderService;
    }

    @GetMapping(value = "/mealordered")
    public List<MealOrderedDTO> getAllMealOrdered() {
        return mealOrderedService.getAll().stream().map(MealOrderedDTO::from).collect(Collectors.toList());
    }

    @GetMapping(value = "/mealordered/{id}")
    public MealOrderedDTO getMealOrdered(@PathVariable @Valid Long id) {
        return MealOrderedDTO.from(mealOrderedService.get(id));
    }

    @PostMapping(value = "/mealordered")
    public void createMealOrdered(@RequestBody @Valid CreateMealOrderedDTO createMealOrderedDTO) {
        Order order = orderService.get(createMealOrderedDTO.orderId);
        Restaurant restaurant = restaurantService.get(createMealOrderedDTO.restaurantId);
        MealOrdered mealOrdered = MealOrdered.from(createMealOrderedDTO, order, restaurant);
        mealOrderedService.create(mealOrdered);
    }

    @PutMapping(value = "/mealordered/{id}")
    public void updateMealOrdered(@PathVariable @Valid Long id, @RequestBody @Valid CreateMealOrderedDTO createMealOrderedDTO) {
        MealOrdered mealOrdered = mealOrderedService.get(id);
        mealOrderedService.update(MealOrdered.update(mealOrdered, createMealOrderedDTO));
    }

    @DeleteMapping(value = "/mealordered/{id}")
    public void deleteMealOrdered(@PathVariable @Valid Long id) {
        mealOrderedService.delete(id);
    }
}
