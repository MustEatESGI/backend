package fr.esgi.musteat.backend.user.exposition.controller;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.exposition.dto.AddressCodingDTO;
import fr.esgi.musteat.backend.location.infrastructure.service.LocationService;
import fr.esgi.musteat.backend.user.domain.User;
import fr.esgi.musteat.backend.user.exposition.dto.CreateUserDTO;
import fr.esgi.musteat.backend.user.exposition.dto.UserDTO;
import fr.esgi.musteat.backend.user.infrastructure.service.UserService;
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
public class UserController {

    private final UserService userService;

    private final LocationService locationService;

    public UserController(UserService userService, LocationService locationService) {
        this.userService = userService;
        this.locationService = locationService;
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAll().stream().map(UserDTO::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable @Valid Long id) {
        User user = userService.get(id);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserDTO.from(userService.get(id)));
    }

    @PostMapping(value = "/user")
    public ResponseEntity<String> createUser(@RequestBody @Valid CreateUserDTO createUserDTO) {
        AddressCodingDTO addressCodingDTO = locationService.getLocationFromAddress(createUserDTO.location);
        Location location = Location.from(addressCodingDTO);
        locationService.create(location);

        User user = User.from(createUserDTO, location);
        userService.create(user);
        return ResponseEntity.created(linkTo(methodOn(UserController.class).getUserById(user.getId())).toUri()).body(user.getId().toString());
    }

    @PutMapping(value = "/user/{id}")
    public ResponseEntity<String> updateUser(@PathVariable @Valid Long id, @RequestBody @Valid CreateUserDTO createUserDTO) {
        User user = userService.get(id);
        AddressCodingDTO addressCodingDTO = locationService.getLocationFromAddress(createUserDTO.location);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        locationService.update(Location.update(user.getLocation(), addressCodingDTO));
        userService.update(User.update(user, createUserDTO, addressCodingDTO));
        return ResponseEntity.created(linkTo(methodOn(UserController.class).getUserById(user.getId())).toUri()).build();
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable @Valid Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
