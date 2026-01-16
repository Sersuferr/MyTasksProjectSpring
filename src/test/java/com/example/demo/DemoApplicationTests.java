package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testCreateAndGetProduct() throws Exception {
        String productJson = """
            {
                "name": "Test Product",
                "description": "Test Description",
                "price": 99.99,
                "quantityInStock": 10
            }
            """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Product"));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void testProductValidation() throws Exception {
        String invalidProductJson = """
            {
                "name": "",
                "price": -10,
                "quantityInStock": -5
            }
            """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidProductJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetNonExistentProduct() throws Exception {
        mockMvc.perform(get("/api/products/9999"))
                .andExpect(status().isNotFound());
    }
}