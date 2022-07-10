package fr.esgi.musteat.backend.user.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserValidatorTest {

    private static UserValidator userValidator;
    private static String name;
    private static String password;

    @BeforeAll
    static void setup() {
        userValidator = new UserValidator();
        name = "name";
        password = "password";
    }

    @Test
    void should_be_valid() {
        User user = new User(name, password, null);
        assertThatNoException().isThrownBy(() -> userValidator.validate(user));
    }

    @Test
    void should_be_invalid_when_user_is_null() {
        assertThatThrownBy(() -> userValidator.validate(null)).isInstanceOf(IllegalArgumentException.class).hasMessage("User is null");
    }

    @Test
    void should_be_invalid_when_name_is_null() {
        User user = new User(null, password, null);
        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(IllegalArgumentException.class).hasMessage("User name is null or empty");
    }

    @Test
    void should_be_invalid_when_name_is_empty() {
        User user = new User("", password, null);
        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(IllegalArgumentException.class).hasMessage("User name is null or empty");
    }

    @Test
    void should_be_invalid_when_password_is_null() {
        User user = new User(name, null, null);
        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(IllegalArgumentException.class).hasMessage("User password is null or empty");
    }

    @Test
    void should_be_invalid_when_password_is_empty() {
        User user = new User(name, "", null);
        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(IllegalArgumentException.class).hasMessage("User password is null or empty");
    }
}
