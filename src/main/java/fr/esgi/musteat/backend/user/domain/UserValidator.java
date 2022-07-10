package fr.esgi.musteat.backend.user.domain;

import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.location.domain.LocationValidator;

public class UserValidator implements Validator<User> {

    private final LocationValidator locationValidator = new LocationValidator();

    @Override
    public void validate(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("User name is null or empty");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("User password is null or empty");
        }

        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("User password is too short");
        }

        if (user.getPassword().length() > 255) {
            throw new IllegalArgumentException("User password is too long");
        }

        if (!user.getPassword().matches(".*\\d.*")) {
            throw new IllegalArgumentException("User password must contain at least one number");
        }

        if (!user.getPassword().matches(".*[a-z].*")) {
            throw new IllegalArgumentException("User password must contain at least one lowercase letter");
        }

        if (!user.getPassword().matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("User password must contain at least one uppercase letter");
        }

        if (user.getLocation() == null) {
            throw new IllegalArgumentException("User location is null");
        }

        locationValidator.validate(user.getLocation());
    }
}
