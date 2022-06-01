package fr.esgi.musteat.backend.order.exposition.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateOrderDTO {

    public LocalDateTime orderDate = LocalDateTime.now();
    @NotNull
    public Long userId;
    @NotNull
    public Long restaurantId;
}
