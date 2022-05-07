package fr.esgi.musteat.backend.meal.exposition.controller;

import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.exposition.dto.CreateMealDTO;
import fr.esgi.musteat.backend.meal.exposition.dto.MealDTO;
import fr.esgi.musteat.backend.meal.infrastructure.service.MealService;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.infrastructure.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<MealDTO> getMeals() {
        return mealService.getAll().stream().map(MealDTO::from).collect(Collectors.toList());
    }

    @GetMapping(value = "/meal/{id}")
    public MealDTO getMeal(@PathVariable @Valid Long id) {
        return MealDTO.from(mealService.get(id));
    }

    @PostMapping(value = "/meal")
    public void createMeal(@RequestBody @Valid CreateMealDTO createMealDTO) {
        Restaurant restaurant = restaurantService.get(createMealDTO.restaurantId);
        Meal meal = Meal.from(createMealDTO, restaurant);
        mealService.create(meal);
    }

    @PutMapping(value = "/meal/{id}")
    public void updateMeal(@PathVariable @Valid Long id, @RequestBody @Valid CreateMealDTO createMealDTO) {
        Meal meal = mealService.get(id);
        mealService.update(Meal.update(meal, createMealDTO));
    }

    @DeleteMapping(value = "/meal/{id}")
    public void deleteMeal(@PathVariable @Valid Long id) {
        mealService.delete(id);
    }
}
