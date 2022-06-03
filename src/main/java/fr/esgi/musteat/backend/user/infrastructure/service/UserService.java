package fr.esgi.musteat.backend.user.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.user.domain.User;
import fr.esgi.musteat.backend.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@org.springframework.stereotype.Service
@Transactional
@Slf4j
public class UserService extends Service<UserRepository, User, Long> implements UserDetailsService {

    public UserService(UserRepository repository, Validator<User> validator) {
        super(repository, validator, "user");
    }

    public User findByUsername(String username) {
         return repository.getAll()
                 .stream()
                 .filter((user) -> user.getName() == username)
                 .findFirst()
                 .orElseThrow();

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.findByUsername(username);
        if(user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }else {
            log.info(user.toString());
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("AUTHENTICATED"));
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
    }
}
