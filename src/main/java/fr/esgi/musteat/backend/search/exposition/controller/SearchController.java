package fr.esgi.musteat.backend.search.exposition.controller;

import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.infrastructure.service.MealService;
import fr.esgi.musteat.backend.search.domain.SortType;
import fr.esgi.musteat.backend.search.exposition.dto.MealSearchedDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class SearchController {

    MealService mealService;

    public SearchController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping("/search/{mealName}/{sort}")
    public ResponseEntity<List<MealSearchedDTO>> searchByName(@PathVariable @Valid String mealName, @PathVariable @Valid String sort) {
        List<Meal> meals = sortMeals(mealService.findByName(mealName), SortType.fromString(sort));
        return ResponseEntity.status(HttpStatus.OK)
                .body(meals.stream().map(MealSearchedDTO::from).collect(Collectors.toList()));
    }
    
    private List<Meal> sortMeals(List<Meal> meals, SortType sort) {
        switch (sort) {
            case Price:
                return meals.stream().sorted(Comparator.comparing(Meal::getPrice)).collect(Collectors.toList());
            case Distance:
                return meals.stream().sorted(Comparator.comparing(Meal::getDistance)).collect(Collectors.toList());
            case Both:
                return meals.stream().sorted(Comparator.comparing(Meal::getPrice).thenComparing(Meal::getDistance)).collect(Collectors.toList());
            default:
                return meals;
        }
    }
}
