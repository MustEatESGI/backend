package fr.esgi.musteat.backend.location.exposition.dto;

import fr.esgi.musteat.backend.location.domain.Location;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class LocationDTO {

    @NotNull
    public Long id;
    @NotNull
    public Double latitude;

    @NotNull
    public Double longitude;

    public LocationDTO(Long id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LocationDTO from(Location location) {
        return new LocationDTO(location.getId(), location.getLatitude(), location.getLongitude());
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationDTO that = (LocationDTO) o;
        return Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
