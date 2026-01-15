package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.OrderSummary.class)
    private Long id;

    @JsonView(View.OrderSummary.class)
    @Column(name = "order_number")
    private String orderNumber;

    @NotBlank(message = "Товары обязательны")
    @JsonView(View.OrderSummary.class)
    @Column(columnDefinition = "TEXT")
    private String items;

    @NotNull(message = "Сумма обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Сумма должна быть больше 0")
    @JsonView(View.OrderSummary.class)
    private BigDecimal totalAmount;

    @NotBlank(message = "Статус обязателен")
    @JsonView(View.OrderSummary.class)
    private String status;

    @JsonView(View.OrderSummary.class)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonView(View.OrderDetails.class)
    @JsonIgnoreProperties({"orders"})
    private User user;

    public Order() {}

    public Order(String items, BigDecimal totalAmount, String status, User user) {
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.user = user;
        this.orderNumber = "ORD-" + System.currentTimeMillis();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (orderNumber == null) {
            orderNumber = "ORD-" + System.currentTimeMillis();
        }
    }
}