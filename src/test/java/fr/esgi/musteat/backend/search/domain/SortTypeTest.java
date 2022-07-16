package fr.esgi.musteat.backend.search.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SortTypeTest {

    @Test
    void fromString_should_return_SortType_DISTANCE_when_text_is_distance() {
        assertEquals(SortType.DISTANCE, SortType.fromString("distance"));
    }

    @Test
    void fromString_should_return_SortType_PRICE_when_text_is_price() {
        assertEquals(SortType.PRICE, SortType.fromString("price"));
    }

    @Test
    void fromString_should_return_SortType_RATIO_when_text_is_ratio() {
        assertEquals(SortType.RATIO, SortType.fromString("ratio"));
    }

    @Test
    void fromString_should_return_SortType_RATIO_when_text_is_empty() {
        assertEquals(SortType.RATIO, SortType.fromString(""));
    }

    @Test
    void fromString_should_throw_IllegalArgumentException_when_text_is_not_distance_price_ratio_or_empty() {
        assertThrows(IllegalArgumentException.class, () -> SortType.fromString("foo"));
    }
}
