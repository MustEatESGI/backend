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

        return ResponseEntity.status(HttpStatus.OK).body(MealOrderedDTO.from(mealOrdered));
    }

    @PostMapping(value = "/mealordered")
    public ResponseEntity<String> createMealOrdered(@RequestBody @Valid CreateMealOrderedDTO createMealOrderedDTO) {
        Order order = orderService.get(createMealOrderedDTO.orderId);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        try {
            MealOrdered mealOrdered = MealOrdered.from(createMealOrderedDTO, order);
            mealOrderedService.create(mealOrdered);
            return ResponseEntity.created(linkTo(methodOn(MealOrderedController.class).getMealOrdered(mealOrdered.getId())).toUri()).body(mealOrdered.getId().toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(value = "/mealordered/{id}")
    public ResponseEntity<String> updateMealOrdered(@PathVariable @Valid Long id, @RequestBody @Valid CreateMealOrderedDTO createMealOrderedDTO) {
        Order order = orderService.get(createMealOrderedDTO.orderId);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        MealOrdered mealOrdered = mealOrderedService.get(id);

        if (mealOrdered == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("MealOrdered not found");
        }

        try {
            mealOrderedService.update(MealOrdered.update(mealOrdered, createMealOrderedDTO, order));
            return ResponseEntity.created(linkTo(methodOn(MealOrderedController.class).getMealOrdered(mealOrdered.getId())).toUri()).body(mealOrdered.getId().toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/mealordered/{id}")
    public ResponseEntity<String> deleteMealOrdered(@PathVariable @Valid Long id) {
        try {
            mealOrderedService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
