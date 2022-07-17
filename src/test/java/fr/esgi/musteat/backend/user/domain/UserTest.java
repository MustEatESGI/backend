package fr.esgi.musteat.backend.user.domain;

import fr.esgi.musteat.backend.kernel.EntityTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest extends EntityTest<User> {

    public UserTest() {
        super(new User(1L, "name", "password", null), new User(1L, "name", "password", null));
    }

    @Test
    void testToString() {
        User user = new User(1L, "name", "password", null);
        assertEquals("User{id=1, name='name', password='password', location=null}", user.toString());
    }
}
