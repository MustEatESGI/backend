package fr.esgi.musteat.backend.restaurant.exposition.controller;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.infrastructure.service.LocationService;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.infrastructure.service.MealService;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.exposition.dto.CreateRestaurantDTO;
import fr.esgi.musteat.backend.restaurant.exposition.dto.RestaurantDTO;
import fr.esgi.musteat.backend.restaurant.exposition.dto.RestaurantDetailsDTO;
import fr.esgi.musteat.backend.restaurant.infrastructure.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class RestaurantController {

    private final RestaurantService restaurantService;

    private final LocationService locationService;

    private final MealService mealService;

    public RestaurantController(RestaurantService restaurantService, LocationService locationService, MealService mealService) {
        this.restaurantService = restaurantService;
        this.locationService = locationService;
        this.mealService = mealService;
    }

    @GetMapping(value = "/restaurants")
    public List<RestaurantDTO> getRestaurants() {
        return restaurantService.getAll().stream().map(RestaurantDTO::from).collect(Collectors.toList());
    }

    @GetMapping(value = "/restaurant/{id}")
    public RestaurantDetailsDTO getRestaurant(@PathVariable @Valid Long id) {
        List<Meal> meals = mealService.findByRestaurantId(id);
        return RestaurantDetailsDTO.from(restaurantService.get(id), meals);
    }

    @PostMapping(value = "/restaurant")
    public void createRestaurant(@RequestBody @Valid CreateRestaurantDTO createRestaurantDTO) {
        Location location = Location.from(createRestaurantDTO.location);
        locationService.create(location);

        Restaurant restaurant = Restaurant.from(createRestaurantDTO, location);
        restaurantService.create(restaurant);
    }

    @PutMapping(value = "/restaurant/{id}")
    public void updateRestaurant(@PathVariable @Valid Long id, @RequestBody @Valid CreateRestaurantDTO createRestaurantDTO) {
        Restaurant restaurant = restaurantService.get(id);

        locationService.update(Location.update(restaurant.getLocation().getId(), createRestaurantDTO.location));
        restaurantService.update(Restaurant.update(restaurant, createRestaurantDTO));
    }

    @DeleteMapping(value = "/restaurant/{id}")
    public void deleteRestaurant(@PathVariable @Valid Long id) {
        restaurantService.delete(id);
    }
}
