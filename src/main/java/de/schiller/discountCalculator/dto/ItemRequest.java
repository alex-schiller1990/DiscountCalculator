package de.schiller.discountCalculator.dto;

import java.math.BigDecimal;

public record ItemRequest(
        String productName,
        BigDecimal price,
        Integer quantity
) {
}
