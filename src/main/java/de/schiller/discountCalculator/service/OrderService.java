package de.schiller.discountCalculator.service;

import de.schiller.discountCalculator.dto.ItemRequest;
import de.schiller.discountCalculator.dto.OrderRequest;
import de.schiller.discountCalculator.dto.OrderResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class OrderService {

    public OrderResponse createOrder(OrderRequest orderRequest) {
        BigDecimal totalAmount = calculateTotalAmount(orderRequest.items());
        double discountPercentage = getDiscountPercentage(totalAmount);
        BigDecimal discountedAmount = calculateDiscountedAmount(totalAmount, discountPercentage);

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

    //TODO
    private double getDiscountPercentage(BigDecimal totalAmount) {
        return 0;
    }

    private BigDecimal calculateDiscountedAmount(BigDecimal totalAmount, double discountPercentage) {
        BigDecimal discountValue = totalAmount.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(discountPercentage));
        return totalAmount.subtract(discountValue);
    }
}
