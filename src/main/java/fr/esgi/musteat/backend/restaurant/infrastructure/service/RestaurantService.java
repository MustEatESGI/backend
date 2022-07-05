package fr.esgi.musteat.backend.restaurant.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.restaurant.domain.RestaurantRepository;
import fr.esgi.musteat.backend.restaurant.exposition.dto.RestaurantDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class RestaurantService extends Service<RestaurantRepository, Restaurant, Long> {

    public RestaurantService(RestaurantRepository repository, Validator<Restaurant> validator) {
        super(repository, validator, "restaurant");
    }
}
