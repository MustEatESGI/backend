package fr.esgi.musteat.backend.fixtures.exposition.controller;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.infrastructure.service.LocationService;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.infrastructure.service.MealService;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.infrastructure.service.RestaurantService;

public class FixturesController {

    LocationService locationService;
    MealService mealService;
    RestaurantService restaurantService;

    private Location locationFixture;
    private Meal mealFixture;
    private Restaurant restaurantFixture;

    public FixturesController(RestaurantService restaurantService, MealService mealService, LocationService locationService) {
        this.restaurantService = restaurantService;
        this.mealService = mealService;
        this.locationService = locationService;
    }

    public void resetFixtures() {
        this.cleanLocationFixtures();
        this.cleanMealFixtures();
        this.cleanRestaurantFixtures();
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
}
