package fr.esgi.musteat.backend.user.domain;

import fr.esgi.musteat.backend.location.domain.Location;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserValidatorTest {

    private static UserValidator userValidator;
    private static String name;
    private static String password;
    private static Location location;

    @BeforeAll
    static void setup() {
        userValidator = new UserValidator();
        name = "name";
        password = "Password1";
        location = new Location(10.0, 10.0);
    }

    @Test
    void should_be_valid() {
        User user = new User(name, password, location);
        assertThatNoException().isThrownBy(() -> userValidator.validate(user));
    }

    @Test
    void should_be_invalid_when_user_is_null() {
        assertThatThrownBy(() -> userValidator.validate(null)).isInstanceOf(IllegalArgumentException.class).hasMessage("User is null");
    }

    @Test
    void should_be_invalid_when_name_is_null() {
        User user = new User(null, password, location);
        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(IllegalArgumentException.class).hasMessage("User name is null or empty");
    }

    @Test
    void should_be_invalid_when_name_is_empty() {
        User user = new User("", password, location);
        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(IllegalArgumentException.class).hasMessage("User name is null or empty");
    }

    @Test
    void should_be_invalid_when_password_is_null() {
        User user = new User(name, null, location);
        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(IllegalArgumentException.class).hasMessage("User password is null or empty");
    }

    @Test
    void should_be_invalid_when_password_is_empty() {
        User user = new User(name, "", location);
        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(IllegalArgumentException.class).hasMessage("User password is null or empty");
    }

    @Test
    void should_be_invalid_when_password_is_less_than_8_characters() {
        User user = new User(name, "1234567", location);
        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(IllegalArgumentException.class).hasMessage("User password is too short");
    }

    @Test
    void should_be_invalid_when_password_is_more_than_255_characters() {
        User user = new User(name, "a".repeat(256), location);
        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(IllegalArgumentException.class).hasMessage("User password is too long");
    }

    @Test
    void should_be_invalid_when_password_does_not_contain_at_least_one_number() {
        User user = new User(name, "abcdefgh", location);
        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(IllegalArgumentException.class).hasMessage("User password must contain at least one number");
    }

    @Test
    void should_be_invalid_when_password_does_not_contain_at_least_one_lowercase_letter() {
        User user = new User(name, "ABCDEFGH1", location);
        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(IllegalArgumentException.class).hasMessage("User password must contain at least one lowercase letter");
    }

    @Test
    void should_be_invalid_when_password_does_not_contain_at_least_one_uppercase_letter() {
        User user = new User(name, "abcdefgh1", location);
        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(IllegalArgumentException.class).hasMessage("User password must contain at least one uppercase letter");
    }

    @Test
    void should_be_invalid_when_location_is_null() {
        User user = new User(name, password, null);
        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(IllegalArgumentException.class).hasMessage("User location is null");
    }
}
