package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Customer is required")
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @NotEmpty(message = "Order must contain at least one product")
    private List<Product> products;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order() {
        this.orderDate = LocalDateTime.now();
        this.orderStatus = OrderStatus.PENDING;
    }

    public Order(Customer customer, List<Product> products, String shippingAddress) {
        this();
        this.customer = customer;
        this.products = products;
        this.shippingAddress = shippingAddress;
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        if (products != null && !products.isEmpty()) {
            this.totalPrice = products.stream()
                    .map(Product::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.totalPrice = BigDecimal.ZERO;
        }
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        calculateTotalPrice();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        calculateTotalPrice();
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}

enum OrderStatus {
    PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
}