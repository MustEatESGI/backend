package fr.esgi.musteat.backend.restaurant.infrastructure.repository;

import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.domain.RestaurantRepository;
import fr.esgi.musteat.backend.restaurant.infrastructure.database.RestaurantDB;
import fr.esgi.musteat.backend.restaurant.infrastructure.database.RestaurantDBRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InDBRestaurantRepository implements RestaurantRepository {

    private final RestaurantDBRepository dbRepository;

    public InDBRestaurantRepository(RestaurantDBRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    @Override
    public Optional<Restaurant> get(Long key) {
        Optional<RestaurantDB> restaurantDB = dbRepository.findById(key);
        return restaurantDB.map(RestaurantDB::toRestaurant);
    }

    @Override
    public Long add(Restaurant value) {
        RestaurantDB restaurantDB = dbRepository.save(RestaurantDB.fromRestaurant(value));
        value.setId(restaurantDB.getId());
        return restaurantDB.getId();
    }

    @Override
    public boolean update(Restaurant value) {
        if (dbRepository.findById(value.getId()).isEmpty()) {
            return false;
        }
        dbRepository.save(RestaurantDB.fromRestaurant(value));
        return true;
    }

    @Override
    public boolean remove(Long value) {
        if (dbRepository.findById(value).isEmpty()) {
            return false;
        }
        dbRepository.delete(dbRepository.findById(value).get());
        return true;
    }

    @Override
    public List<Restaurant> getAll() {
        List<Restaurant> restaurants = new ArrayList<>();
        dbRepository.findAll().forEach(restaurantDB -> restaurants.add(restaurantDB.toRestaurant()));
        return restaurants;
    }
}
