package de.schiller.discountCalculator.service;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static void assertResponse(OrderResponse response, String customerName, BigDecimal totalAmount, BigDecimal discountedAmount, double discountedPercentage) {
        assertEquals(customerName, response.customerName());
        assertEquals(0, totalAmount.compareTo(response.totalAmount()),
                () -> String.format("Expected Total Amount: %s, but was: %s", totalAmount, response.totalAmount()));
        assertEquals(0, discountedAmount.compareTo(response.discountedAmount()),
                () -> String.format("Expected Discounted Amount: %s, but was: %s", discountedAmount, response.discountedAmount()));
        assertEquals(discountedPercentage, response.discountPercentage());
    }

    @Test
    void testCreateOrderWithOneProduct() {
        List<ItemRequest> items = List.of(new ItemRequest("p1", BigDecimal.ONE, 1));
        OrderRequest orderRequest = new OrderRequest("customer1", items);

        when(discountService.getDiscountPercentage(BigDecimal.ONE)).thenReturn(0.0);
        when(discountService.calculateDiscountedAmount(BigDecimal.ONE, 0)).thenReturn(BigDecimal.ONE);

        OrderResponse response = orderService.createOrder(orderRequest);
        assertResponse(response, "customer1", BigDecimal.ONE, BigDecimal.ONE, 0);
    }

    @Test
    void testCreateOrderWithOneProductMultipleQuantity() {
        List<ItemRequest> items = List.of(new ItemRequest("p1", BigDecimal.TWO, 3));
        OrderRequest orderRequest = new OrderRequest("customer2", items);

        BigDecimal bigDecimalSix = BigDecimal.valueOf(6);
        when(discountService.getDiscountPercentage(bigDecimalSix)).thenReturn(0.0);
        when(discountService.calculateDiscountedAmount(bigDecimalSix, 0)).thenReturn(bigDecimalSix);

        OrderResponse response = orderService.createOrder(orderRequest);
        assertResponse(response, "customer2", bigDecimalSix, bigDecimalSix, 0);
    }

    @Test
    void testCreateOrderWithMultipleProductMultipleQuantity() {
        List<ItemRequest> items = List.of(
                new ItemRequest("p1", BigDecimal.TWO, 3),
                new ItemRequest("p2", BigDecimal.valueOf(100), 2),
                new ItemRequest("p3", BigDecimal.valueOf(500), 1));
        OrderRequest orderRequest = new OrderRequest("customer3", items);

        BigDecimal expectedTotal = BigDecimal.valueOf(706);
        BigDecimal expectDiscount = BigDecimal.valueOf(635.4);
        when(discountService.getDiscountPercentage(expectedTotal)).thenReturn(10.0);
        when(discountService.calculateDiscountedAmount(expectedTotal, 10)).thenReturn(expectDiscount);

        OrderResponse response = orderService.createOrder(orderRequest);
        assertResponse(response, "customer3", expectedTotal, expectDiscount, 10);
    }

}