package de.schiller.discountCalculator.dto;

import java.util.List;

/*
Request Body for Post Request /orders
 */
public record OrderRequest(
        String customerName,
        List<ItemRequest> items
) {
}
