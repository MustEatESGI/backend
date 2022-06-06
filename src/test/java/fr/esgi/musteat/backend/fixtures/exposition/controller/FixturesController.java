package fr.esgi.musteat.backend.fixtures.exposition.controller;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.infrastructure.service.LocationService;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.infrastructure.service.MealService;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.infrastructure.service.RestaurantService;
import fr.esgi.musteat.backend.user.domain.User;
import fr.esgi.musteat.backend.user.infrastructure.service.UserService;

public class FixturesController {

    LocationService locationService;
    MealService mealService;
    RestaurantService restaurantService;
    UserService userService;

    private Location locationFixture;
    private Meal mealFixture;
    private Restaurant restaurantFixture;
    private User userFixture;

    public FixturesController(
            LocationService locationService,
            MealService mealService,
            RestaurantService restaurantService,
            UserService userService) {
        this.mealService = mealService;
        this.locationService = locationService;
        this.restaurantService = restaurantService;
        this.userService = userService;
    }

    public void resetFixtures() {
        this.cleanLocationFixtures();
        this.cleanMealFixtures();
        this.cleanRestaurantFixtures();
        this.cleanUserFixtures();
    }

    public Location getLocationFixture() {
        if (this.locationFixture == null) {
            this.addLocationFixture();
        }
        return this.locationFixture;
    }

    public Meal getMealFixture() {
        if (this.mealFixture == null) {
            this.addMealFixture();
        }
        return this.mealFixture;
    }

    public Restaurant getRestaurantFixtures() {
        if (this.restaurantFixture == null) {
            this.addRestaurantFixtures();
        }
        return this.restaurantFixture;
    }

    public User getUserFixture() {
        if (this.userFixture == null) {
            this.addUserFixture();
        }
        return this.userFixture;
    }

    public void addLocationFixture() {
        Location location = new Location(40.0, 2.0);
        this.locationService.create(location);
        this.locationFixture = location;
    }

    public void addMealFixture() {
        Meal meal = new Meal("fixtureMeal", 10_00L, getRestaurantFixtures());
        this.mealService.create(meal);
        this.mealFixture = meal;
    }

    public void addRestaurantFixtures() {
        Restaurant restaurant = new Restaurant("fixtureRestaurant", getLocationFixture());
        this.restaurantService.create(restaurant);
        this.restaurantFixture = restaurant;
    }

    public void addUserFixture() {
        User user = new User("fixtureUser", "password", getLocationFixture());
        this.userService.create(user);
        this.userFixture = user;
    }

    public void cleanLocationFixtures() {
        if (this.locationFixture != null) {
            this.locationService.delete(this.locationFixture.getId());
            this.locationFixture = null;
        }
    }

    public void cleanMealFixtures() {
        if (this.mealFixture != null) {
            this.mealService.delete(this.mealFixture.getId());
            this.mealFixture = null;
        }
    }

    public void cleanRestaurantFixtures() {
        if (this.restaurantFixture != null) {
            this.restaurantService.delete(this.restaurantFixture.getId());
            this.restaurantFixture = null;
        }
    }

    public void cleanUserFixtures() {
        if (this.userFixture != null) {
            this.userService.delete(this.userFixture.getId());
            this.userFixture = null;
        }
    }
}
