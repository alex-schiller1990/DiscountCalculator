package de.schiller.discountCalculator.repository;

import de.schiller.discountCalculator.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
