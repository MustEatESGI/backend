package fr.esgi.musteat.backend.user.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Repository;
import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.ServiceTest;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.user.domain.User;
import fr.esgi.musteat.backend.user.domain.UserRepository;
import fr.esgi.musteat.backend.user.domain.UserValidator;
import fr.esgi.musteat.backend.user.infrastructure.repository.InMemoryUserRepositoryTest;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest extends ServiceTest<UserRepository, User, Long> {

    private final PasswordEncoder passwordEncoder = new PasswordEncoder() {
        @Override
        public String encode(CharSequence rawPassword) {
            return rawPassword.toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return rawPassword.toString().equals(encodedPassword);
        }
    };

    public UserServiceTest() {
        super(
                new User(0L, "user", "Password1",
                        new Location(10.0, 10.0)),
                new User(0L, "new name", "Password2",
                        new Location(10.0, 10.0)));
    }

    @Override
    protected UserRepository getRepository() {
        return new InMemoryUserRepositoryTest();
    }

    @Override
    protected Service<UserRepository, User, Long> getService(Repository repository) {
        return new UserService((UserRepository) repository, new UserValidator(), passwordEncoder);
    }
}
