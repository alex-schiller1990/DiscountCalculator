package de.schiller.discountCalculator.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/*
Request Body for Post Request /orders
 */
public record OrderRequest(
        @NotBlank (message= "Customer Name is required")
        String customerName,
        @Valid
        @NotEmpty (message = "At least one item is required")
        List<ItemRequest> items
) {
}
