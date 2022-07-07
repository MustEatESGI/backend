package fr.esgi.musteat.backend.order.exposition.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class CreateOrderDTO {

    @NotNull
    public LocalDateTime orderDate = LocalDateTime.now();
    @NotNull
    public Long userId;
    @NotNull
    public Long restaurantId;
    @NotNull
    @NotEmpty
    public List<Long> mealsId;
}
