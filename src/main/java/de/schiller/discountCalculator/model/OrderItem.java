package de.schiller.discountCalculator.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class OrderItem {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String productName;
        private BigDecimal price; // Price of single item
        private Integer quantity;

        @ManyToOne
        @JoinColumn(name = "order_id", nullable = false)
        private Order order;

        public OrderItem() {
        }

        public OrderItem(Long id, String productName, BigDecimal price, Integer quantity, Order order) {
                this.id = id;
                this.productName = productName;
                this.price = price;
                this.quantity = quantity;
                this.order = order;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getProductName() {
                return productName;
        }

        public void setProductName(String productName) {
                this.productName = productName;
        }

        public BigDecimal getPrice() {
                return price;
        }

        public void setPrice(BigDecimal price) {
                this.price = price;
        }

        public Integer getQuantity() {
                return quantity;
        }

        public void setQuantity(Integer quantity) {
                this.quantity = quantity;
        }

        public Order getOrder() {
                return order;
        }

        public void setOrder(Order order) {
                this.order = order;
        }
}
