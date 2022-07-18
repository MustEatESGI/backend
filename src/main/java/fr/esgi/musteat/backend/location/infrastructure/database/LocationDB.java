package fr.esgi.musteat.backend.location.infrastructure.database;

import fr.esgi.musteat.backend.location.domain.Location;

import javax.persistence.*;

@Table(name = "location")
@Entity
public class LocationDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double latitude;
    private Double longitude;

    public LocationDB() {
    }

    private LocationDB(Long id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LocationDB of(Location location) {
        return new LocationDB(
                location.getId(),
                location.getLatitude(),
                location.getLongitude());
    }

    public static LocationDB fromLocation(Location location) {
        return new LocationDB(
                location.getId(),
                location.getLatitude(),
                location.getLongitude());
    }

    public Location toLocation() {
        return new Location(
                this.id,
                this.latitude,
                this.longitude);
    }

    public Long getId() {
        return id;
    }
}
