package fr.esgi.musteat.backend.search.exposition.controller;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.infrastructure.service.MealService;
import fr.esgi.musteat.backend.search.domain.SortType;
import fr.esgi.musteat.backend.search.exposition.dto.MealSearchedDTO;
import fr.esgi.musteat.backend.security.JWTService;
import fr.esgi.musteat.backend.user.infrastructure.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class SearchController {

    MealService mealService;
    UserService userService;

    public SearchController(MealService mealService, UserService userService) {
        this.mealService = mealService;
        this.userService = userService;
    }

    @GetMapping("/search/{mealName}/{sort}")
    public ResponseEntity<List<MealSearchedDTO>> searchByName(@PathVariable @Valid String mealName, @PathVariable @Valid String sort, HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        Location userLocation = userService.findByUsername(JWTService.extractSubjectFromBearerToken(authorizationHeader)).getLocation();
        List<Meal> meals = sortMeals(mealService.findByName(mealName), SortType.fromString(sort), userLocation);
        return ResponseEntity.status(HttpStatus.OK)
                .body(meals.stream().map(meal -> MealSearchedDTO.from(meal, userLocation)).collect(Collectors.toList()));
    }

    private List<Meal> sortMeals(List<Meal> meals, SortType sort, Location userLocation) {
        switch (sort) {
            case PRICE:
                return meals.stream().sorted(Comparator.comparing(Meal::getPrice)).collect(Collectors.toList());
            case DISTANCE:
                return meals.stream().sorted(Comparator.comparing(meal -> meal.getDistance(userLocation))).collect(Collectors.toList());
            case RATIO:
                return meals.stream().sorted(Comparator.comparing(Meal::getPrice).thenComparing(meal -> meal.getDistance(userLocation))).collect(Collectors.toList());
            default:
                return meals;
        }
    }
}
