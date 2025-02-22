package de.schiller.discountCalculator.dto;

import java.math.BigDecimal;

public record OrderResponse(
        Long orderId,
        String customerName,
        BigDecimal totalAmount,
        BigDecimal discountedAmount,
        Double discountPercentage
) {
}
