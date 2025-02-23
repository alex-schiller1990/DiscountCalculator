package de.schiller.discountCalculator.service;

import de.schiller.discountCalculator.dto.ItemRequest;
import de.schiller.discountCalculator.dto.OrderRequest;
import de.schiller.discountCalculator.dto.OrderResponse;
import de.schiller.discountCalculator.model.Order;
import de.schiller.discountCalculator.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private DiscountService discountService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private void initOrderRepositorySaveMock() {
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            return new Order(1L, order.getCustomerName(), order.getTotalAmount(), order.getDiscountedAmount(), order.getDiscountPercentage(), order.getOrderItems());
        });
    }

    private static void assertOrderResponse(OrderResponse response, Long orderID, String customerName, BigDecimal totalAmount, BigDecimal discountedAmount, double discountedPercentage) {
        assertEquals(orderID, response.orderId());
        assertEquals(customerName, response.customerName());
        assertEquals(0, totalAmount.compareTo(response.totalAmount()),
                () -> String.format("Expected Total Amount: %s, but was: %s", totalAmount, response.totalAmount()));
        assertEquals(0, discountedAmount.compareTo(response.discountedAmount()),
                () -> String.format("Expected Discounted Amount: %s, but was: %s", discountedAmount, response.discountedAmount()));
        assertEquals(discountedPercentage, response.discountPercentage());
    }

    @Test
    void testCreateOrder_WithOneProduct() {
        List<ItemRequest> items = List.of(new ItemRequest("p1", BigDecimal.ONE, 1));
        OrderRequest orderRequest = new OrderRequest("customer1", items);

        initOrderRepositorySaveMock();

        when(discountService.getDiscountPercentage(BigDecimal.ONE)).thenReturn(0.0);
        when(discountService.calculateDiscountedAmount(BigDecimal.ONE, 0)).thenReturn(BigDecimal.ONE);

        OrderResponse response = orderService.createOrder(orderRequest);
        assertOrderResponse(response, 1L, "customer1", BigDecimal.ONE, BigDecimal.ONE, 0);
    }

    @Test
    void testCreateOrder_WithOneProduct_MultipleQuantity() {
        List<ItemRequest> items = List.of(new ItemRequest("p1", BigDecimal.TWO, 3));
        OrderRequest orderRequest = new OrderRequest("customer2", items);

        initOrderRepositorySaveMock();

        BigDecimal bigDecimalSix = BigDecimal.valueOf(6);
        when(discountService.getDiscountPercentage(bigDecimalSix)).thenReturn(0.0);
        when(discountService.calculateDiscountedAmount(bigDecimalSix, 0)).thenReturn(bigDecimalSix);

        OrderResponse response = orderService.createOrder(orderRequest);
        assertOrderResponse(response, 1L, "customer2", bigDecimalSix, bigDecimalSix, 0);
    }

    @Test
    void testCreateOrder_WithMultipleProduct_MultipleQuantity() {
        List<ItemRequest> items = List.of(
                new ItemRequest("p1", BigDecimal.TWO, 3),
                new ItemRequest("p2", BigDecimal.valueOf(100), 2),
                new ItemRequest("p3", BigDecimal.valueOf(500), 1));
        OrderRequest orderRequest = new OrderRequest("customer3", items);

        initOrderRepositorySaveMock();

        BigDecimal expectedTotal = BigDecimal.valueOf(706);
        BigDecimal expectDiscount = BigDecimal.valueOf(635.4);
        when(discountService.getDiscountPercentage(expectedTotal)).thenReturn(10.0);
        when(discountService.calculateDiscountedAmount(expectedTotal, 10)).thenReturn(expectDiscount);

        OrderResponse response = orderService.createOrder(orderRequest);
        assertOrderResponse(response, 1L, "customer3", expectedTotal, expectDiscount, 10);
    }

    @Test
    void testGetOrderById() {
        Order order = new Order(1L, "customer", BigDecimal.ONE, BigDecimal.ONE, 0.0, List.of());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.getOrderById(1L);
        assertOrderResponse(response, 1L, "customer", BigDecimal.ONE, BigDecimal.ONE, 0);
    }

    @Test
    void testGetAllOrders() {
        List<Order> orders = List.of(
                new Order(1L, "customer", BigDecimal.ONE, BigDecimal.ONE, 0.0, List.of()),
                new Order(2L, "customer2", BigDecimal.TWO, BigDecimal.TWO, 0.0, List.of())
        );
        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderResponse> result = orderService.getAllOrders();

        assertEquals(orders.size(), result.size());
        assertOrderResponse(result.getFirst(), 1L, "customer", BigDecimal.ONE, BigDecimal.ONE, 0.0);
        assertOrderResponse(result.getLast(), 2L, "customer2", BigDecimal.TWO, BigDecimal.TWO, 0.0);
    }

}