package de.schiller.discountCalculator.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public record OrderItem(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id,
        String productName,
        BigDecimal price, // Price of single Item
        Integer quantity,
        @ManyToOne
        @JoinColumn (name="order_id", nullable = false)
        Order order
) {
}
