package fr.esgi.musteat.backend.user.exposition.controller;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.location.infrastructure.service.LocationService;
import fr.esgi.musteat.backend.user.domain.User;
import fr.esgi.musteat.backend.user.exposition.dto.CreateUserDTO;
import fr.esgi.musteat.backend.user.exposition.dto.UserDTO;
import fr.esgi.musteat.backend.user.infrastructure.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<UserDTO> getUsers() {
        return userService.getAll().stream().map(UserDTO::from).collect(Collectors.toList());
    }

    @GetMapping(value = "/user/{id}")
    public UserDTO getUserById(@PathVariable @Valid Long id) {
        return UserDTO.from(userService.get(id));
    }

    @PostMapping(value = "/user")
    public void createUser(@RequestBody @Valid CreateUserDTO createUserDTO) {
        Location location = Location.from(createUserDTO.location);
        locationService.create(location);

        User user = User.from(createUserDTO, location);
        userService.create(user);
    }

    @PutMapping(value = "/user/{id}")
    public void updateUser(@PathVariable @Valid Long id, @RequestBody @Valid CreateUserDTO createUserDTO) {
        User user = userService.get(id);

        locationService.update(Location.update(user.getLocation().getId(), createUserDTO.location));
        userService.update(User.update(user, createUserDTO));
    }

    @DeleteMapping(value = "/user/{id}")
    public void deleteUser(@PathVariable @Valid Long id) {
        userService.delete(id);
    }
}
