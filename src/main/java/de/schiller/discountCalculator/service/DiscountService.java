package de.schiller.discountCalculator.service;

import de.schiller.discountCalculator.config.DiscountSettings;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeMap;

@Service
public class DiscountService {

    private final DiscountSettings discountSettings;

    public DiscountService(DiscountSettings discountSettings) {
        this.discountSettings = discountSettings;
    }


    public double getDiscountPercentage(BigDecimal totalAmount) {
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

    public BigDecimal calculateDiscountedAmount(BigDecimal totalAmount, double discountPercentage) {
        BigDecimal discountValue = totalAmount.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(discountPercentage));
        return totalAmount.subtract(discountValue);
    }
}
