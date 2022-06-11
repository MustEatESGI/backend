package fr.esgi.musteat.backend.user.domain;

import fr.esgi.musteat.backend.kernel.Entity;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.user.exposition.dto.CreateUserDTO;

import javax.persistence.Column;

public class User extends Entity<Long> {

    @Column(unique = true)
    private final String name;
    private final String password;
    private final Location location;

    public User(Long id, String name, String password, Location location) {
        super(id);
        this.name = name;
        this.password = password;
        this.location = location;
    }

    public User(String name, String password, Location location) {
        super(null);
        this.name = name;
        this.password = password;
        this.location = location;
    }

    public static User from(CreateUserDTO createUserDTO, Location location) {
        return new User(createUserDTO.username, createUserDTO.password, location);
    }

    public static User update(User user, CreateUserDTO createUserDTO) {
        return new User(user.getId(), createUserDTO.username, createUserDTO.password, Location.update(user.getLocation().getId(), createUserDTO.location));
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", location=" + location +
                '}';
    }
}
