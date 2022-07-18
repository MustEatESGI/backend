package fr.esgi.musteat.backend.kernel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class EntityTest<T> {

    protected T firstValue;
    protected T secondValue;

    public EntityTest(T firstValue, T secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    @Test
    void testEquals() {
        assertEquals(firstValue, secondValue);
    }

    @Test
    void testHashCode() {
        assertEquals(firstValue.hashCode(), secondValue.hashCode());
    }
}
