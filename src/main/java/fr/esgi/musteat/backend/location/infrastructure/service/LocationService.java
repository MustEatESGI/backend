package fr.esgi.musteat.backend.location.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.domain.LocationRepository;
import fr.esgi.musteat.backend.location.exposition.dto.AddressCodingDTO;
import fr.esgi.musteat.backend.location.exposition.dto.CreateLocationDTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

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
        final String uri = "http://api.positionstack.com/v1/forward?access_key=" + positionStackApiKey + "&query=" + createLocationDTO.address.replace(" ", "+") + "&country=fr";

        RestTemplate restTemplate = new RestTemplate();
        // TODO: Catcher l'erreur si l'utilisateur rentre une adresse invalide
        JSONObject coordinates = new JSONObject(restTemplate.getForObject(uri, String.class)).getJSONArray("data").getJSONObject(0);

        return new AddressCodingDTO(coordinates.getDouble("latitude"), coordinates.getDouble("longitude"));
    }

    public Long getTimeBetweenTwoLocations(Location firstLocation, Location secondLocation) {
        final String uri = "https://maps.googleapis.com/maps/api/distancematrix/json?key=" + googleMapsStackApiKey +
                "&origins=" + firstLocation.getLatitude() + "," + firstLocation.getLongitude() +
                "&destinations=" + secondLocation.getLatitude() + "," + secondLocation.getLongitude();

        RestTemplate restTemplate = new RestTemplate();
        // TODO: Catcher l'erreur si l'utilisateur rentre une adresse invalide
        JSONObject jsonObject = new JSONObject(restTemplate.getForObject(uri, String.class));

        return jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getLong("value");
    }
}
