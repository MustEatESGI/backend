package fr.esgi.musteat.backend.order.exposition.controller;

import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.order.exposition.dto.CreateOrderDTO;
import fr.esgi.musteat.backend.order.exposition.dto.OrderDTO;
import fr.esgi.musteat.backend.order.infrastructure.service.OrderService;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.infrastructure.service.RestaurantService;
import fr.esgi.musteat.backend.user.domain.User;
import fr.esgi.musteat.backend.user.infrastructure.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class OrderController {

    private final OrderService orderService;

    private final UserService userService;

    private final RestaurantService restaurantService;

    public OrderController(OrderService orderService, UserService userService, RestaurantService restaurantService) {
        this.orderService = orderService;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @GetMapping(value = "/orders")
    public ResponseEntity<List<OrderDTO>> getOrders() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getAll().stream().map(OrderDTO::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/order/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable @Valid Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(OrderDTO.from(orderService.get(id)));
    }

    @PostMapping(value = "/order")
    public ResponseEntity createOrder(@RequestBody @Valid CreateOrderDTO createOrderDTO) {
        User user = userService.get(createOrderDTO.userId);
        Restaurant restaurant = restaurantService.get(createOrderDTO.restaurantId);
        Order order = Order.from(createOrderDTO, user, restaurant);
        orderService.create(order);
        return ResponseEntity.created(linkTo(methodOn(OrderController.class).getOrder(order.getId())).toUri()).build();
    }

    @PutMapping(value = "/order/{id}")
    public ResponseEntity updateOrder(@PathVariable @Valid Long id, @RequestBody @Valid CreateOrderDTO createOrderDTO) {
        Order order = orderService.get(id);
        orderService.update(Order.update(order, createOrderDTO));
        return ResponseEntity.created(linkTo(methodOn(OrderController.class).getOrder(order.getId())).toUri()).build();
    }

    @DeleteMapping(value = "/order/{id}")
    public ResponseEntity deleteOrder(@PathVariable @Valid Long id) {
        orderService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
