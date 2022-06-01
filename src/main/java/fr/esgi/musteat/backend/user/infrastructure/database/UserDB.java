package fr.esgi.musteat.backend.user.infrastructure.database;

import fr.esgi.musteat.backend.location.infrastructure.database.LocationDB;
import fr.esgi.musteat.backend.user.domain.User;

import javax.persistence.*;

@Table(name = "\"user\"")
@Entity
public class UserDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String password;

    @OneToOne
    private LocationDB location;

    protected UserDB() {
    }

    public UserDB(Long id, String name, String password, LocationDB location) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.location = location;
    }

    public static UserDB fromUser(User user) {
        return new UserDB(user.getId(), user.getName(), user.getPassword(), LocationDB.fromLocation(user.getLocation()));
    }

    public User toUser() {
        return new User(this.id, this.name, this.password, this.location.toLocation());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public LocationDB getLocation() {
        return location;
    }
}
