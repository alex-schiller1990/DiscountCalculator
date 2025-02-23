package de.schiller.discountCalculator.service;

import de.schiller.discountCalculator.config.DiscountSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DiscountServiceTest {

    @Mock
    private DiscountSettings discountSettings;

    @InjectMocks
    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "99.99, 0",
            "100, 5",
            "499.99, 5",
            "500, 10",
            "999.99, 10",
            "1000, 15",
            "10000, 15"
    })
    void testGetDiscountPercentage(BigDecimal totalAmount, Double expectedDiscountPercentage) {
        Map<Double, Double> minValueMap = Map.of(
                0.0, 0.0,
                100.0, 5.0,
                500.0, 10.0,
                1000.0, 15.0
        );
        when(discountSettings.getMinValue()).thenReturn(minValueMap);

        double discountPercentage = discountService.getDiscountPercentage(totalAmount);
        assertEquals(expectedDiscountPercentage, discountPercentage);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 0",
            "99.99, 0, 99.99",
            "100, 5, 95",
            "499.99, 5, 474.99",
            "500, 10, 450",
            "999.99, 10, 899.99",
            "1000, 15, 850",
            "10000, 15, 8500"
    })
    void testCalculateDiscountedAmount(BigDecimal totalAmount, Double discountPercentage, BigDecimal expectedDiscountedAmount) {
        BigDecimal calculatedDiscountedAmount = discountService.calculateDiscountedAmount(totalAmount, discountPercentage);
        assertEquals(0, expectedDiscountedAmount.compareTo(calculatedDiscountedAmount),
                () -> String.format("Expected: %s, but was: %s", expectedDiscountedAmount, calculatedDiscountedAmount));
    }
}