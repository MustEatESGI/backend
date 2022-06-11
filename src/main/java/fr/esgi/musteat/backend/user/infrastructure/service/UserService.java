package fr.esgi.musteat.backend.user.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.user.domain.User;
import fr.esgi.musteat.backend.user.domain.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@org.springframework.stereotype.Service
@Transactional
@Slf4j
public class UserService extends Service<UserRepository, User, Long> implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, Validator<User> validator, PasswordEncoder passwordEncoder) {
        super(repository, validator, "user");
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) {
         return repository.getAll()
                 .stream()
                 .filter(user -> user.getName().equals(username))
                 .findFirst()
                 .orElseThrow();

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found in the database");
        }else {
            log.info(user.toString());
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("AUTHENTICATED"));
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
    }

    @Override
    public void create(User entity) {
        User user = new User(entity.getName(), passwordEncoder.encode(entity.getPassword()), entity.getLocation());
        super.create(user);
    }
}
