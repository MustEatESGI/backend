package fr.esgi.musteat.backend.mealordered.exposition.controller;

import fr.esgi.musteat.backend.mealordered.domain.MealOrdered;
import fr.esgi.musteat.backend.mealordered.exposition.dto.CreateMealOrderedDTO;
import fr.esgi.musteat.backend.mealordered.exposition.dto.MealOrderedDTO;
import fr.esgi.musteat.backend.mealordered.infrastructure.service.MealOrderedService;
import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.order.infrastructure.service.OrderService;
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
public class MealOrderedController {

    private final MealOrderedService mealOrderedService;

    private final OrderService orderService;

    public MealOrderedController(MealOrderedService mealOrderedService, OrderService orderService) {
        this.mealOrderedService = mealOrderedService;
        this.orderService = orderService;
    }

    @GetMapping(value = "/mealordered")
    public ResponseEntity<List<MealOrderedDTO>> getAllMealOrdered() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(mealOrderedService.getAll().stream().map(MealOrderedDTO::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/mealordered/{id}")
    public ResponseEntity<MealOrderedDTO> getMealOrdered(@PathVariable @Valid Long id) {
        MealOrdered mealOrdered = mealOrderedService.get(id);

        if (mealOrdered == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(MealOrderedDTO.from(mealOrdered));
    }

    @PostMapping(value = "/mealordered")
    public ResponseEntity createMealOrdered(@RequestBody @Valid CreateMealOrderedDTO createMealOrderedDTO) {
        Order order = orderService.get(createMealOrderedDTO.orderId);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        MealOrdered mealOrdered = MealOrdered.from(createMealOrderedDTO, order);
        mealOrderedService.create(mealOrdered);
        return ResponseEntity.created(linkTo(methodOn(MealOrderedController.class).getMealOrdered(mealOrdered.getId())).toUri()).build();
    }

    @PutMapping(value = "/mealordered/{id}")
    public ResponseEntity updateMealOrdered(@PathVariable @Valid Long id, @RequestBody @Valid CreateMealOrderedDTO createMealOrderedDTO) {
        Order order = orderService.get(createMealOrderedDTO.orderId);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        MealOrdered mealOrdered = mealOrderedService.get(id);

        if (mealOrdered == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("MealOrdered not found");
        }

        mealOrderedService.update(MealOrdered.update(mealOrdered, createMealOrderedDTO, order));
        return ResponseEntity.created(linkTo(methodOn(MealOrderedController.class).getMealOrdered(mealOrdered.getId())).toUri()).build();
    }

    @DeleteMapping(value = "/mealordered/{id}")
    public ResponseEntity deleteMealOrdered(@PathVariable @Valid Long id) {
        mealOrderedService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
