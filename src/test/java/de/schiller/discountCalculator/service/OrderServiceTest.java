package de.schiller.discountCalculator.service;

import de.schiller.discountCalculator.dto.ItemRequest;
import de.schiller.discountCalculator.dto.OrderRequest;
import de.schiller.discountCalculator.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static void assertResponse(OrderResponse response, String customerName, BigDecimal totalAmount, BigDecimal discountedAmount, double discountedPercentage) {
        assertEquals(customerName, response.customerName());
        assertEquals(0, totalAmount.compareTo(response.totalAmount()));
        assertEquals(0, discountedAmount.compareTo(response.discountedAmount()));
        assertEquals(discountedPercentage, response.discountPercentage());
    }

    @Test
    void testCreateOrderWithNoDiscount() {
        List<ItemRequest> items = List.of(new ItemRequest("p1", BigDecimal.ONE, 1));
        OrderRequest orderRequest = new OrderRequest("customer1", items);

        OrderResponse response = orderService.createOrder(orderRequest);
        assertResponse(response, "customer1", BigDecimal.ONE, BigDecimal.ONE, 0);
    }

    @Test
    void testCreateOrderWithNoDiscountMultipleProducts() {
        List<ItemRequest> items = List.of(new ItemRequest("p1", BigDecimal.ONE, 2), new ItemRequest("p2", BigDecimal.valueOf(97.99), 1));
        OrderRequest orderRequest = new OrderRequest("customer2", items);

        OrderResponse response = orderService.createOrder(orderRequest);
        assertResponse(response, "customer2", BigDecimal.valueOf(99.99), BigDecimal.valueOf(99.99), 0);
    }

}