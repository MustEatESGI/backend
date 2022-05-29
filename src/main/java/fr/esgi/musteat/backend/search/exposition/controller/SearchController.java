package fr.esgi.musteat.backend.search.exposition.controller;

import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.exposition.dto.MealDetailsDTO;
import fr.esgi.musteat.backend.meal.infrastructure.service.MealService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class SearchController {

    MealService mealService;

    public SearchController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping("/search/{mealName}")
    public ResponseEntity<List<MealDetailsDTO>> searchByName(@PathVariable @Valid String mealName) {
        List<Meal> meals = mealService.findByName(mealName);
        return ResponseEntity.status(HttpStatus.OK)
                .body(meals.stream().map(MealDetailsDTO::from).collect(Collectors.toList()));
    }
}
