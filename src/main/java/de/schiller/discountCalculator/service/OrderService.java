package de.schiller.discountCalculator.service;

import de.schiller.discountCalculator.dto.ItemRequest;
import de.schiller.discountCalculator.dto.OrderRequest;
import de.schiller.discountCalculator.dto.OrderResponse;
import de.schiller.discountCalculator.model.Order;
import de.schiller.discountCalculator.model.OrderItem;
import de.schiller.discountCalculator.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final DiscountService discountService;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(DiscountService discountService, OrderRepository orderRepository) {
        this.discountService = discountService;
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {
        BigDecimal totalAmount = calculateTotalAmount(orderRequest.items());
        double discountPercentage = discountService.getDiscountPercentage(totalAmount);
        BigDecimal discountedAmount = discountService.calculateDiscountedAmount(totalAmount, discountPercentage);

        Order order = new Order(null, orderRequest.customerName(), totalAmount, discountedAmount, discountPercentage, null);
        List<OrderItem> orderItems = orderRequest.items().stream().map(item ->
                new OrderItem(null, item.productName(), item.price(), item.quantity(), order))
                .toList();
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        return buildOrderResponse(savedOrder);
    }

    private BigDecimal calculateTotalAmount(List<ItemRequest> items) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ItemRequest item : items) {
            totalAmount = totalAmount.add(item.price().multiply(new BigDecimal(item.quantity())));
        }
        return totalAmount;
    }

    public OrderResponse getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(this::buildOrderResponse).orElse(null);
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::buildOrderResponse).toList();
    }

    private OrderResponse buildOrderResponse(Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getCustomerName(),
                order.getTotalAmount(),
                order.getDiscountedAmount(),
                order.getDiscountPercentage());
    }

    public void deleteOrderById(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found with id: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

}
