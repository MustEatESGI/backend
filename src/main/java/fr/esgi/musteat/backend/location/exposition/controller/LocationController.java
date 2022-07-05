package fr.esgi.musteat.backend.location.exposition.controller;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.exposition.dto.AddressCodingDTO;
import fr.esgi.musteat.backend.location.exposition.dto.CreateLocationDTO;
import fr.esgi.musteat.backend.location.exposition.dto.LocationDTO;
import fr.esgi.musteat.backend.location.infrastructure.service.LocationService;
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
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping(value = "/locations")
    public ResponseEntity<List<LocationDTO>> getLocations() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(locationService.getAll().stream().map(LocationDTO::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/location/{id}")
    public ResponseEntity<LocationDTO> getLocation(@PathVariable @Valid Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(LocationDTO.from(locationService.get(id)));
    }

    @PostMapping(value = "/location")
    public ResponseEntity createLocation(@RequestBody @Valid CreateLocationDTO createLocationDTO) {
        AddressCodingDTO addressCodingDTO = locationService.getLocationFromAddress(createLocationDTO);
        Location location = Location.from(addressCodingDTO);
        locationService.create(location);
        return ResponseEntity.created(linkTo(methodOn(LocationController.class).getLocation(location.getId())).toUri()).build();
    }

    @PutMapping(value = "/location/{id}")
    public ResponseEntity updateLocation(@PathVariable @Valid Long id, @RequestBody @Valid  CreateLocationDTO createLocationDTO) {
        Location location = locationService.get(id);
        AddressCodingDTO addressCodingDTO = locationService.getLocationFromAddress(createLocationDTO);
        locationService.update(Location.update(location, addressCodingDTO));
        return ResponseEntity.ok(linkTo(methodOn(LocationController.class).getLocation(location.getId())).toUri());
    }

    @DeleteMapping(value = "/location/{id}")
    public ResponseEntity deleteLocation(@PathVariable @Valid Long id) {
        locationService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
