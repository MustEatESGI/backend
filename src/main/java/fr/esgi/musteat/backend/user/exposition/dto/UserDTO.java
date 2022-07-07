package fr.esgi.musteat.backend.user.exposition.dto;

import fr.esgi.musteat.backend.location.exposition.dto.LocationDTO;
import fr.esgi.musteat.backend.user.domain.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(name, userDTO.name) && Objects.equals(location, userDTO.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location);
    }
}
