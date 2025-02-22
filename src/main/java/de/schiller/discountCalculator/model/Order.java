package de.schiller.discountCalculator.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "purchase_order") // order is a reserved keyword
public record Order(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long orderId,
        String customerName,
        BigDecimal totalAmount,
        BigDecimal discountedAmount,
        Double discountPercentage,
        @OneToMany(mappedBy = "order")
        List<OrderItem> orderItems
) {
}
