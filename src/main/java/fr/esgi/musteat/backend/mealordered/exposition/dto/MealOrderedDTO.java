package fr.esgi.musteat.backend.mealordered.exposition.dto;

import fr.esgi.musteat.backend.mealordered.domain.MealOrdered;
import fr.esgi.musteat.backend.order.exposition.dto.OrderDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MealOrderedDTO {

    @NotNull
    public Long id;
    @NotNull
    @NotBlank
    public String name;
    @NotNull
    public Long price;
    @NotNull
    public OrderDTO orderDTO;

    public MealOrderedDTO(Long id, String name, Long price, OrderDTO orderDTO) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.orderDTO = orderDTO;
    }

    public static MealOrderedDTO from(MealOrdered mealOrdered) {
        return new MealOrderedDTO(mealOrdered.getId(), mealOrdered.getName(), mealOrdered.getPrice(), OrderDTO.from(mealOrdered.getOrder()));
    }
}
