package fr.esgi.musteat.backend.order.exposition.controller;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.infrastructure.service.LocationService;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.infrastructure.service.MealService;
import fr.esgi.musteat.backend.mealordered.domain.MealOrdered;
import fr.esgi.musteat.backend.mealordered.infrastructure.service.MealOrderedService;
import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.order.exposition.dto.CreateOrderDTO;
import fr.esgi.musteat.backend.order.exposition.dto.OrderDTO;
import fr.esgi.musteat.backend.order.infrastructure.service.OrderService;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.infrastructure.service.RestaurantService;
import fr.esgi.musteat.backend.security.JWTService;
import fr.esgi.musteat.backend.user.domain.User;
import fr.esgi.musteat.backend.user.infrastructure.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class OrderController {

    private final MealService mealService;

    private final MealOrderedService mealOrderedService;

    private final OrderService orderService;

    private final UserService userService;

    private final RestaurantService restaurantService;

    private final LocationService locationService;

    public OrderController(MealService mealService, MealOrderedService mealOrderedService, OrderService orderService, UserService userService, RestaurantService restaurantService, LocationService locationService) {
        this.mealService = mealService;
        this.mealOrderedService = mealOrderedService;
        this.orderService = orderService;
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.locationService = locationService;
    }

    @GetMapping(value = "/orders")
    public ResponseEntity<List<OrderDTO>> getOrders() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getAll().stream().map(OrderDTO::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/order/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable @Valid Long id, @RequestHeader("Authorization") String authorizationHeader) {
        Location userLocation = userService.findByUsername(JWTService.extractSubjectFromBearerToken(authorizationHeader)).getLocation();
        Order order = orderService.get(id);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(OrderDTO.from(order, locationService.getTimeBetweenTwoLocations(userLocation, order.getRestaurant().getLocation())));
    }

    @PostMapping(value = "/order")
    public ResponseEntity<String> createOrder(@RequestBody @Valid CreateOrderDTO createOrderDTO, @RequestHeader("Authorization") String authorizationHeader) {
        List<Meal> orderedMeals = new ArrayList<>();
        User user = userService.get(createOrderDTO.userId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Restaurant restaurant = restaurantService.get(createOrderDTO.restaurantId);

        if (restaurant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurant not found");
        }

        for (Long mealId : createOrderDTO.mealsId) {
            Meal meal = mealService.get(mealId);

            if (meal == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Meal not found");
            }

            orderedMeals.add(meal);
        }

        Order order = Order.from(createOrderDTO, user, restaurant);
        orderService.create(order);
        orderedMeals.forEach(orderedMeal -> mealOrderedService.create(MealOrdered.from(orderedMeal, order)));
        return ResponseEntity.created(linkTo(methodOn(OrderController.class).getOrder(order.getId(), authorizationHeader)).toUri()).build();
    }

    @PutMapping(value = "/order/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable @Valid Long id, @RequestBody @Valid CreateOrderDTO createOrderDTO, @RequestHeader("Authorization") String authorizationHeader) {
        Order order = orderService.get(id);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        User user = userService.get(createOrderDTO.userId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Restaurant restaurant = restaurantService.get(createOrderDTO.restaurantId);

        if (restaurant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurant not found");
        }

        for (Long mealId : createOrderDTO.mealsId) {
            Meal meal = mealService.get(mealId);

            if (meal == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Meal not found");
            }
        }

        orderService.update(Order.update(order, createOrderDTO));
        return ResponseEntity.created(linkTo(methodOn(OrderController.class).getOrder(order.getId(), authorizationHeader)).toUri()).build();
    }

    @DeleteMapping(value = "/order/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable @Valid Long id) {
        orderService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
