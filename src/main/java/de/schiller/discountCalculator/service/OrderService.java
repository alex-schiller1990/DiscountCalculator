package de.schiller.discountCalculator.service;

import de.schiller.discountCalculator.config.DiscountSettings;
import de.schiller.discountCalculator.dto.ItemRequest;
import de.schiller.discountCalculator.dto.OrderRequest;
import de.schiller.discountCalculator.dto.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class OrderService {

    private final DiscountSettings discountSettings;

    @Autowired
    public OrderService(DiscountSettings discountSettings) {
        this.discountSettings = discountSettings;
    }

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

    private double getDiscountPercentage(BigDecimal totalAmount) {
        double discountPercentage = 0;
        Map<Double, Double> minValueMap = new TreeMap<>(discountSettings.getMinValue());
        for (Map.Entry<Double, Double> entry : minValueMap.entrySet()) {
            if (totalAmount.compareTo(BigDecimal.valueOf(entry.getKey())) > -1) {
                discountPercentage = entry.getValue();
            } else {
                break;
            }
        }
        return discountPercentage;
    }

    private BigDecimal calculateDiscountedAmount(BigDecimal totalAmount, double discountPercentage) {
        BigDecimal discountValue = totalAmount.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(discountPercentage));
        return totalAmount.subtract(discountValue);
    }
}
