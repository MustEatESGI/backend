package fr.esgi.musteat.backend.meal.exposition.controller;

import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.exposition.dto.CreateMealDTO;
import fr.esgi.musteat.backend.meal.exposition.dto.MealDetailsDTO;
import fr.esgi.musteat.backend.meal.infrastructure.service.MealService;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.infrastructure.service.RestaurantService;
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
public class MealController {

    private final MealService mealService;

    private final RestaurantService restaurantService;

    public MealController(MealService mealService, RestaurantService restaurantService) {
        this.mealService = mealService;
        this.restaurantService = restaurantService;
    }

    @GetMapping(value = "/meals")
    public ResponseEntity<List<MealDetailsDTO>> getMeals() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(mealService.getAll().stream().map(MealDetailsDTO::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/meal/{id}")
    public ResponseEntity<MealDetailsDTO> getMeal(@PathVariable @Valid Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(MealDetailsDTO.from(mealService.get(id)));
    }

    @PostMapping(value = "/meal")
    public ResponseEntity createMeal(@RequestBody @Valid CreateMealDTO createMealDTO) {
        Restaurant restaurant = restaurantService.get(createMealDTO.restaurantId);
        Meal meal = Meal.from(createMealDTO, restaurant);
        mealService.create(meal);
        return ResponseEntity.created(linkTo(methodOn(MealController.class).getMeal(meal.getId())).toUri()).build();
    }

    @PutMapping(value = "/meal/{id}")
    public ResponseEntity updateMeal(@PathVariable @Valid Long id, @RequestBody @Valid CreateMealDTO createMealDTO) {
        Meal meal = mealService.get(id);
        mealService.update(Meal.update(meal, createMealDTO));
        return ResponseEntity.ok(linkTo(methodOn(MealController.class).getMeal(meal.getId())).toUri());
    }

    @DeleteMapping(value = "/meal/{id}")
    public ResponseEntity deleteMeal(@PathVariable @Valid Long id) {
        mealService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
