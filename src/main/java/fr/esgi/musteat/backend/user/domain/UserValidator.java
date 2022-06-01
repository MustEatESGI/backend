package fr.esgi.musteat.backend.user.domain;

import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.location.domain.LocationValidator;

public class UserValidator implements Validator<User> {

    private final LocationValidator locationValidator;

    public UserValidator(LocationValidator locationValidator) {
        this.locationValidator = locationValidator;
    }

    @Override
    public void validate(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("User name is null or empty");
        }


    }
}
