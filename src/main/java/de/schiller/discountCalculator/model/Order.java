package de.schiller.discountCalculator.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "purchase_order") // order is a reserved keyword
public class Order {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long orderId;

        private String customerName;
        private BigDecimal totalAmount;
        private BigDecimal discountedAmount;
        private Double discountPercentage;

        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<OrderItem> orderItems;

        public Order() {
        }

        public Order(Long orderId, String customerName, BigDecimal totalAmount, BigDecimal discountedAmount, Double discountPercentage, List<OrderItem> orderItems) {
                this.orderId = orderId;
                this.customerName = customerName;
                this.totalAmount = totalAmount;
                this.discountedAmount = discountedAmount;
                this.discountPercentage = discountPercentage;
                this.orderItems = orderItems;
        }

        public Long getOrderId() {
                return orderId;
        }

        public void setOrderId(Long orderId) {
                this.orderId = orderId;
        }

        public String getCustomerName() {
                return customerName;
        }

        public void setCustomerName(String customerName) {
                this.customerName = customerName;
        }

        public BigDecimal getTotalAmount() {
                return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
                this.totalAmount = totalAmount;
        }

        public BigDecimal getDiscountedAmount() {
                return discountedAmount;
        }

        public void setDiscountedAmount(BigDecimal discountedAmount) {
                this.discountedAmount = discountedAmount;
        }

        public Double getDiscountPercentage() {
                return discountPercentage;
        }

        public void setDiscountPercentage(Double discountPercentage) {
                this.discountPercentage = discountPercentage;
        }

        public List<OrderItem> getOrderItems() {
                return orderItems;
        }

        public void setOrderItems(List<OrderItem> orderItems) {
                this.orderItems = orderItems;
        }
}
