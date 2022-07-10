package fr.esgi.musteat.backend.order.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderValidatorTest {

    private static OrderValidator orderValidator;
    private static LocalDateTime orderDate;

    @BeforeAll
    public static void setUp() {
        orderValidator = new OrderValidator();
        orderDate = LocalDateTime.now();
    }

    @Test
    public void should_be_valid() {
        Order order = new Order(orderDate, null, null);
        orderValidator.validate(order);
    }

    @Test
    public void should_be_invalid_when_order_is_null() {
        assertThatThrownBy(() -> orderValidator.validate(null)).isInstanceOf(IllegalArgumentException.class).hasMessage("Order is null");
    }

    @Test
    public void should_be_invalid_when_order_date_is_null() {
        Order order = new Order(null, null, null);
        assertThatThrownBy(() -> orderValidator.validate(order)).isInstanceOf(IllegalArgumentException.class).hasMessage("Order date is null");
    }
}
