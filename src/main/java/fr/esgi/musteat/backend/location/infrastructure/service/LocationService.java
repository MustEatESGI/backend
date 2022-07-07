package fr.esgi.musteat.backend.location.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.domain.LocationRepository;
import fr.esgi.musteat.backend.location.exposition.dto.AddressCodingDTO;
import fr.esgi.musteat.backend.location.exposition.dto.CreateLocationDTO;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@org.springframework.stereotype.Service
public class LocationService extends Service<LocationRepository, Location, Long> {

    @Value("${position-stack.api.key:null}")
    private String positionStackApiKey;

    @Value("${google-maps.api.key:null}")
    private String googleMapsStackApiKey;

    public LocationService(LocationRepository repository, Validator<Location> validator) {
        super(repository, validator, "location");
    }

    public AddressCodingDTO getLocationFromAddress(CreateLocationDTO createLocationDTO) {
        final String uri = "http://api.positionstack.com/v1/forward?access_key=" + positionStackApiKey + "&query=" + createLocationDTO.address.replaceAll(" ", "+") + "&country=fr";

        RestTemplate restTemplate = new RestTemplate();
        // TODO: Catcher l'erreur si l'utilisateur rentre une adresse invalide
        LinkedHashMap<String, Object> coordinates = (LinkedHashMap<String, Object>) ((ArrayList) restTemplate.getForObject(uri, JSONObject.class).get("data")).get(0);

        return new AddressCodingDTO(Double.parseDouble(coordinates.get("latitude").toString()), Double.parseDouble(coordinates.get("longitude").toString()));
    }
}
