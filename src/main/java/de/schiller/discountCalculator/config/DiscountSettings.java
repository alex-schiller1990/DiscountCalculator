package de.schiller.discountCalculator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@PropertySource("classpath:discount.properties")
@ConfigurationProperties(prefix="discount.settings")
public class DiscountSettings {
    private Map<Double, Double> minValue;

    public Map<Double, Double> getMinValue() {
        return minValue;
    }

    public void setMinValue(Map<Double, Double> minValue) {
        this.minValue = minValue;
    }
}
