package fr.esgi.musteat.backend.order.domain;

import fr.esgi.musteat.backend.kernel.EntityTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest extends EntityTest<Order> {

    public OrderTest() {
        super(new Order(1L, LocalDateTime.MIN, null, null), new Order(1L, LocalDateTime.MIN, null, null));
    }

    @Test
    void testToString() {
        Order order = new Order(1L, LocalDateTime.MIN, null, null);
        assertEquals("Order{id=1, orderDate=-999999999-01-01T00:00, user=null, restaurant=null}", order.toString());
    }
}
