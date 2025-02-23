package de.schiller.discountCalculator.repository;

import de.schiller.discountCalculator.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
