package fr.esgi.musteat.backend.location.domain;

import fr.esgi.musteat.backend.kernel.Entity;
import fr.esgi.musteat.backend.location.exposition.dto.AddressCodingDTO;

import java.util.Objects;

public class Location extends Entity<Long> {

    private final Double latitude;
    private final Double longitude;

    public Location(Long id, Double latitude, Double longitude) {
        super(id);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(Double latitude, Double longitude) {
        super(null);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Location from(AddressCodingDTO addressCodingDTO) {
        return new Location(addressCodingDTO.latitude, addressCodingDTO.longitude);
    }

    public static Location update(Location location, AddressCodingDTO addressCodingDTO) {
        return new Location(location.getId(), addressCodingDTO.latitude, addressCodingDTO.longitude);
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public long getDistance(Location userLocation) {
        return (long) (Math.sqrt(Math.pow(userLocation.getLatitude() - latitude, 2) + Math.pow(userLocation.getLongitude() - longitude, 2)) * 100);
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Location location = (Location) o;
        return Objects.equals(latitude, location.latitude) && Objects.equals(longitude, location.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), latitude, longitude);
    }
}
