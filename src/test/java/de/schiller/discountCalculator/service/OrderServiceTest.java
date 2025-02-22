package de.schiller.discountCalculator.service;

import de.schiller.discountCalculator.config.DiscountSettings;
import de.schiller.discountCalculator.dto.ItemRequest;
import de.schiller.discountCalculator.dto.OrderRequest;
import de.schiller.discountCalculator.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Mock
    private DiscountSettings discountSettings;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Map<Double, Double> minValueMap = Map.of(
                0.0, 0.0,
                100.0, 5.0,
                500.0, 10.0,
                1000.0, 15.0
        );
        when(discountSettings.getMinValue()).thenReturn(minValueMap);
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

    @Test
    void testCreateOrderWithFivePercentDiscount() {
        List<ItemRequest> items = List.of(new ItemRequest("p1", BigDecimal.valueOf(200), 1));
        OrderRequest orderRequest = new OrderRequest("customer3", items);

        OrderResponse response = orderService.createOrder(orderRequest);
        assertResponse(response, "customer3", BigDecimal.valueOf(200), BigDecimal.valueOf(190), 5);
    }

}