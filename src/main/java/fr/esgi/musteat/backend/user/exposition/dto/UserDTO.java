package fr.esgi.musteat.backend.user.exposition.dto;

import fr.esgi.musteat.backend.location.exposition.dto.LocationDTO;
import fr.esgi.musteat.backend.user.domain.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserDTO {

    @NotNull
    public Long id;
    @NotNull
    @NotBlank
    public String name;
    @NotNull
    public LocationDTO location;

    public UserDTO(Long id, String name, LocationDTO location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public static UserDTO from(User user) {
        return new UserDTO(user.getId(), user.getName(), LocationDTO.from(user.getLocation()));
    }
}
