package de.schiller.discountCalculator.service;

import de.schiller.discountCalculator.dto.ItemRequest;
import de.schiller.discountCalculator.dto.OrderRequest;
import de.schiller.discountCalculator.dto.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final DiscountService discountService;

    @Autowired
    public OrderService(DiscountService discountService) {
        this.discountService = discountService;
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {
        BigDecimal totalAmount = calculateTotalAmount(orderRequest.items());
        double discountPercentage = discountService.getDiscountPercentage(totalAmount);
        BigDecimal discountedAmount = discountService.calculateDiscountedAmount(totalAmount, discountPercentage);

        //TODO Create Order and OrderItem Object and save in Database

        //TODO create Response from the values of the saved Order Object
        return new OrderResponse(null, orderRequest.customerName(), totalAmount, discountedAmount, discountPercentage);
    }

    private BigDecimal calculateTotalAmount(List<ItemRequest> items) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ItemRequest item : items) {
            totalAmount = totalAmount.add(item.price().multiply(new BigDecimal(item.quantity())));
        }
        return totalAmount;
    }

}
