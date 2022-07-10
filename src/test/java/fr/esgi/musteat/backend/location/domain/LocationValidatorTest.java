package fr.esgi.musteat.backend.location.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LocationValidatorTest {

    private static LocationValidator locationValidator;
    private static Double latitude;
    private static Double longitude;

    @BeforeAll
    static void setup() {
        locationValidator = new LocationValidator();
        latitude = 10.0;
        longitude = 10.0;
    }

    @Test
    void should_be_valid() {
        Location location = new Location(latitude, longitude);
        assertThatNoException().isThrownBy(() -> locationValidator.validate(location));
    }

    @Test
    void should_be_invalid_when_location_is_null() {
        assertThatThrownBy(() -> locationValidator.validate(null)).isInstanceOf(IllegalArgumentException.class).hasMessage("Location is null");
    }

    @Test
    void should_be_invalid_when_latitude_is_null() {
        Location location = new Location(null, longitude);
        assertThatThrownBy(() -> locationValidator.validate(location)).isInstanceOf(IllegalArgumentException.class).hasMessage("Location latitude is null");
    }

    @Test
    void should_be_invalid_when_latitude_above_range() {
        Location location = new Location(91.0, longitude);
        assertThatThrownBy(() -> locationValidator.validate(location)).isInstanceOf(IllegalArgumentException.class).hasMessage("Location latitude is out of range");
    }

    @Test
    void should_be_invalid_when_latitude_below_range() {
        Location location = new Location(-91.0, longitude);
        assertThatThrownBy(() -> locationValidator.validate(location)).isInstanceOf(IllegalArgumentException.class).hasMessage("Location latitude is out of range");
    }

    @Test
    void should_be_invalid_when_longitude_is_null() {
        Location location = new Location(latitude, null);
        assertThatThrownBy(() -> locationValidator.validate(location)).isInstanceOf(IllegalArgumentException.class).hasMessage("Location longitude is null");
    }

    @Test
    void should_be_invalid_when_longitude_above_range() {
        Location location = new Location(latitude, 181.0);
        assertThatThrownBy(() -> locationValidator.validate(location)).isInstanceOf(IllegalArgumentException.class).hasMessage("Location longitude is out of range");
    }

    @Test
    void should_be_invalid_when_longitude_below_range() {
        Location location = new Location(latitude, -181.0);
        assertThatThrownBy(() -> locationValidator.validate(location)).isInstanceOf(IllegalArgumentException.class).hasMessage("Location longitude is out of range");
    }
}
