package fr.esgi.musteat.backend.restaurant.exposition.controller;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.exposition.dto.AddressCodingDTO;
import fr.esgi.musteat.backend.location.infrastructure.service.LocationService;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.infrastructure.service.MealService;
import fr.esgi.musteat.backend.order.infrastructure.service.OrderService;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.exposition.dto.CreateRestaurantDTO;
import fr.esgi.musteat.backend.restaurant.exposition.dto.RestaurantDTO;
import fr.esgi.musteat.backend.restaurant.exposition.dto.RestaurantDetailsDTO;
import fr.esgi.musteat.backend.restaurant.infrastructure.service.RestaurantService;
import fr.esgi.musteat.backend.security.JWTService;
import fr.esgi.musteat.backend.user.infrastructure.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class RestaurantController {

    private final RestaurantService restaurantService;

    private final LocationService locationService;

    private final MealService mealService;

    private final OrderService orderService;

    private final UserService userService;

    public RestaurantController(RestaurantService restaurantService, LocationService locationService, MealService mealService, OrderService orderService, UserService userService) {
        this.restaurantService = restaurantService;
        this.locationService = locationService;
        this.mealService = mealService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping(value = "/restaurants")
    public ResponseEntity<List<RestaurantDTO>> getRestaurants(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        Location userLocation = userService.findByUsername(JWTService.extractSubjectFromBearerToken(authorizationHeader)).getLocation();
        return ResponseEntity.status(HttpStatus.OK)
                .body(restaurantService.getAll().stream().map(restaurant -> RestaurantDTO.from(restaurant, userLocation)).collect(Collectors.toList()));
    }

    @GetMapping(value = "/restaurant/{id}")
    public ResponseEntity<RestaurantDetailsDTO> getRestaurant(@PathVariable @Valid Long id) {
        Restaurant restaurant = restaurantService.get(id);

        if (restaurant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Meal> meals = mealService.findByRestaurantId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(RestaurantDetailsDTO.from(restaurantService.get(id), meals));
    }

    @PostMapping(value = "/restaurant")
    public ResponseEntity<String> createRestaurant(@RequestBody @Valid CreateRestaurantDTO createRestaurantDTO) {
        try {
            AddressCodingDTO addressCodingDTO = locationService.getLocationFromAddress(createRestaurantDTO.location);
            Location location = Location.from(addressCodingDTO);
            locationService.create(location);

            Restaurant restaurant = Restaurant.from(createRestaurantDTO, location);
            restaurantService.create(restaurant);
            return ResponseEntity.created(linkTo(methodOn(RestaurantController.class).getRestaurant(restaurant.getId())).toUri()).body(restaurant.getId().toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(value = "/restaurant/{id}")
    public ResponseEntity<String> updateRestaurant(@PathVariable @Valid Long id, @RequestBody @Valid CreateRestaurantDTO createRestaurantDTO) {
        Restaurant restaurant = restaurantService.get(id);

        if (restaurant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurant not found");
        }

        try {
            AddressCodingDTO addressCodingDTO = locationService.getLocationFromAddress(createRestaurantDTO.location);
            locationService.update(Location.update(restaurant.getLocation(), addressCodingDTO));
            restaurantService.update(Restaurant.update(restaurant, createRestaurantDTO));
            return ResponseEntity.created(linkTo(methodOn(RestaurantController.class).getRestaurant(restaurant.getId())).toUri()).body(restaurant.getId().toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/restaurant/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable @Valid Long id) {
        try {
            restaurantService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/restaurants/trending")
    public ResponseEntity<List<RestaurantDTO>> getTrendsRestaurant(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        Location userLocation = userService.findByUsername(JWTService.extractSubjectFromBearerToken(authorizationHeader)).getLocation();
        Map<Restaurant, Long> restaurants = new HashMap<>();

        orderService.getAll().stream()
                .filter(order -> order.getOrderDate().isAfter(LocalDateTime.now().minusDays(7)))
                .forEach(order -> {
                    if (restaurants.containsKey(order.getRestaurant())) {
                        restaurants.put(order.getRestaurant(), restaurants.get(order.getRestaurant()) + 1);
                    } else {
                        restaurants.put(order.getRestaurant(), 1L);
                    }
                });

        List<Restaurant> sortedRestaurants = restaurants.entrySet().stream()
                .sorted(Map.Entry.<Restaurant, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<RestaurantDTO> restaurantDTOs = sortedRestaurants.stream().limit(5).map(restaurant -> RestaurantDTO.from(restaurant, userLocation)).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(restaurantDTOs);
    }
}
