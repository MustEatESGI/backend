package fr.esgi.musteat.backend.restaurant.exposition.controller;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.exposition.dto.AddressCodingDTO;
import fr.esgi.musteat.backend.location.infrastructure.service.LocationService;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.infrastructure.service.MealService;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.exposition.dto.CreateRestaurantDTO;
import fr.esgi.musteat.backend.restaurant.exposition.dto.RestaurantDTO;
import fr.esgi.musteat.backend.restaurant.exposition.dto.RestaurantDetailsDTO;
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
    public ResponseEntity<List<RestaurantDTO>> getRestaurants() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(restaurantService.getAll().stream().map(RestaurantDTO::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/restaurant/{id}")
    public ResponseEntity<RestaurantDetailsDTO> getRestaurant(@PathVariable @Valid Long id) {
        List<Meal> meals = mealService.findByRestaurantId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(RestaurantDetailsDTO.from(restaurantService.get(id), meals));
    }

    @PostMapping(value = "/restaurant")
    public ResponseEntity createRestaurant(@RequestBody @Valid CreateRestaurantDTO createRestaurantDTO) {
        AddressCodingDTO addressCodingDTO = locationService.getLocationFromAddress(createRestaurantDTO.location);
        Location location = Location.from(addressCodingDTO);
        locationService.create(location);

        Restaurant restaurant = Restaurant.from(createRestaurantDTO, location);
        restaurantService.create(restaurant);
        return ResponseEntity.created(linkTo(methodOn(RestaurantController.class).getRestaurant(restaurant.getId())).toUri()).build();
    }

    @PutMapping(value = "/restaurant/{id}")
    public ResponseEntity updateRestaurant(@PathVariable @Valid Long id, @RequestBody @Valid CreateRestaurantDTO createRestaurantDTO) {
        Restaurant restaurant = restaurantService.get(id);

        AddressCodingDTO addressCodingDTO = locationService.getLocationFromAddress(createRestaurantDTO.location);
        locationService.update(Location.update(restaurant.getLocation(), addressCodingDTO));
        restaurantService.update(Restaurant.update(restaurant, createRestaurantDTO));
        return ResponseEntity.ok(linkTo(methodOn(RestaurantController.class).getRestaurant(restaurant.getId())).toUri());
    }

    @DeleteMapping(value = "/restaurant/{id}")
    public ResponseEntity deleteRestaurant(@PathVariable @Valid Long id) {
        restaurantService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
