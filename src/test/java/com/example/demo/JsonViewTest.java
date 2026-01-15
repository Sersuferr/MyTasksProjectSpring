package com.example.demo;

import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.model.View;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class JsonViewTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUserJsonViews() throws Exception {

        User user = new User("Test User", "test@example.com");
        user.setId(1L);

        String summaryJson = objectMapper.writerWithView(View.UserSummary.class)
                .writeValueAsString(user);

        String detailsJson = objectMapper.writerWithView(View.UserDetails.class)
                .writeValueAsString(user);

    }

    @Test
    void testOrderJsonViews() throws Exception {

        Order order = new Order("Test Product", new BigDecimal("99.99"), "NEW", null);
        order.setId(1L);
        order.setOrderNumber("ORD-001");

        String summaryJson = objectMapper.writerWithView(View.OrderSummary.class)
                .writeValueAsString(order);


        String detailsJson = objectMapper.writerWithView(View.OrderDetails.class)
                .writeValueAsString(order);
    }

    @Test
    void testJsonViewWithData() throws Exception {

        User user = new User("John Doe", "john@example.com");
        user.setId(10L);

        Order order = new Order("Laptop", new BigDecimal("1500.00"), "PROCESSING", user);
        order.setId(20L);

        user.getOrders().add(order);

        String userDetailsJson = objectMapper.writerWithView(View.UserDetails.class)
                .writeValueAsString(user);


        String orderDetailsJson = objectMapper.writerWithView(View.OrderDetails.class)
                .writeValueAsString(order);

    }
}