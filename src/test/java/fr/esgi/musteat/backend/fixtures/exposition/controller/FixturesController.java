package fr.esgi.musteat.backend.fixtures.exposition.controller;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.infrastructure.service.LocationService;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.infrastructure.service.RestaurantService;

public class FixturesController {

    RestaurantService restaurantService;
    LocationService locationService;

    private Location locationFixture;
    private Restaurant restaurantFixture;

    public FixturesController(RestaurantService restaurantService, LocationService locationService) {
        this.restaurantService = restaurantService;
        this.locationService = locationService;
    }

    public void resetFixtures() {
        this.cleanLocationFixtures();
        this.cleanRestaurantFixtures();
    }

    public Location getLocationFixture() {
        if (this.locationFixture == null) {
            this.addLocationFixture();
        }
        return this.locationFixture;
    }

    public Restaurant getRestaurantFixtures() {
        if (this.restaurantFixture == null) {
            this.addRestaurantFixtures();
        }
        return this.restaurantFixture;
    }

    private void addLocationFixture() {
        Location location = new Location(40.0, 2.0);
        this.locationService.create(location);
        this.locationFixture = location;
    }

    private void addRestaurantFixtures() {
        Restaurant restaurant = new Restaurant("fixtureRestaurant", getLocationFixture());
        this.restaurantService.create(restaurant);
        this.restaurantFixture = restaurant;
    }

    private void cleanLocationFixtures() {
        this.locationService.delete(this.locationFixture.getId());
    }

    private void cleanRestaurantFixtures() {
        this.restaurantService.delete(this.restaurantFixture.getId());
    }
}
