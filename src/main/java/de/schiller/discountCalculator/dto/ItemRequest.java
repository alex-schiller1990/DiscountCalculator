package de.schiller.discountCalculator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ItemRequest(
        @NotBlank (message = "Product Name is required")
        String productName,
        @PositiveOrZero (message = "Price needs to be positive or zero")
        BigDecimal price,
        @Positive (message = "Quantity needs to be positive")
        Integer quantity
) {
}
