package com.example.demo;

import com.example.demo.controller.OrderController;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void testGetAllOrders() throws Exception {
        User user = new User("Иван Иванов", "ivan@example.com");
        user.setId(1L);

        Order order1 = new Order("Товар 1", new BigDecimal("1000.00"), "PENDING", user);
        order1.setId(1L);

        Order order2 = new Order("Товар 2", new BigDecimal("2000.00"), "DELIVERED", user);
        order2.setId(2L);

        List<Order> orders = Arrays.asList(order1, order2);

        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].items").value("Товар 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].items").value("Товар 2"));
    }

    @Test
    void testGetOrderById() throws Exception {
        User user = new User("Иван Иванов", "ivan@example.com");
        user.setId(1L);

        Order order = new Order("Товар 1", new BigDecimal("1000.00"), "PENDING", user);
        order.setId(1L);

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.items").value("Товар 1"))
                .andExpect(jsonPath("$.totalAmount").value(1000.00))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testCreateOrder() throws Exception {
        User user = new User("Иван Иванов", "ivan@example.com");
        user.setId(1L);

        Order orderToCreate = new Order();
        orderToCreate.setItems("Товар 1");
        orderToCreate.setTotalAmount(new BigDecimal("1000.00"));
        orderToCreate.setStatus("PENDING");

        Order createdOrder = new Order("Товар 1", new BigDecimal("1000.00"), "PENDING", user);
        createdOrder.setId(1L);

        when(orderService.createOrder(anyLong(), any(Order.class))).thenReturn(createdOrder);

        mockMvc.perform(post("/api/orders/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.items").value("Товар 1"));
    }

    @Test
    void testGetOrdersByUserId() throws Exception {
        User user = new User("Иван Иванов", "ivan@example.com");
        user.setId(1L);

        Order order1 = new Order("Товар 1", new BigDecimal("1000.00"), "PENDING", user);
        order1.setId(1L);

        List<Order> orders = Arrays.asList(order1);

        when(orderService.getOrdersByUserId(1L)).thenReturn(orders);

        mockMvc.perform(get("/api/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].items").value("Товар 1"));
    }
}
