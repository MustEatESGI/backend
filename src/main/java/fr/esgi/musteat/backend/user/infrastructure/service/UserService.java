package fr.esgi.musteat.backend.user.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.user.domain.User;
import fr.esgi.musteat.backend.user.domain.UserRepository;

@org.springframework.stereotype.Service
public class UserService extends Service<UserRepository, User, Long> {

    public UserService(UserRepository repository, Validator<User> validator) {
        super(repository, validator, "user");
    }
}
