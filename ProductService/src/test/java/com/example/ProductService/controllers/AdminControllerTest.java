package com.example.ProductService.controllers;

import com.example.ProductService.Models.Product;
import com.example.ProductService.services.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private MockMvc mockMvc;
    Product product = null;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();

        // Set Up Product
        product = new Product();
        product.setName("Laptop");
        product.setDescription("High-performance laptop");
        product.setPrice(BigDecimal.valueOf(999.99));
        product.setStock(50);
        product.setCategory("Electronics");
        product.setSubCategory("Computers");
        product.setActive(true);
        product.setCreatedBy(1);
    }

    @Test
    void addProduct_ShouldReturnOk_WhenUserRoleIsAdmin() throws Exception {

        when(adminService.addProduct(any(Product.class), eq("ADMIN")))
                .thenReturn((ResponseEntity) ResponseEntity.ok(product));

        mockMvc.perform(post("/products/addProduct")
                        .header("X-User-Role", "ADMIN")
                        .content(objectMapper.writeValueAsString(product))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(adminService, times(1)).addProduct(any(Product.class), eq("ADMIN"));
    }
    @Test
    void addProduct_ShouldReturnForbidden_WhenUserRoleIsNotAdmin() throws Exception {
        when(adminService.addProduct(any(Product.class), eq("USER")))
                .thenReturn((ResponseEntity) ResponseEntity.status(403).body("Access Denied: Only ADMIN can add products."));

        mockMvc.perform(post("/products/addProduct")
                        .header("X-User-Role", "USER")
                        .content(objectMapper.writeValueAsString(product))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(adminService, times(1)).addProduct(any(Product.class), eq("USER"));
    }

    @Test
    void addProduct_ShouldReturnNotImplemented_WhenServiceThrowsException() throws Exception {

        when(adminService.addProduct(any(Product.class), eq("ADMIN")))
                .thenReturn((ResponseEntity) ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Product is not saved. Please try again"));

        mockMvc.perform(post("/products/addProduct")
                        .header("X-User-Role", "ADMIN")
                        .content(objectMapper.writeValueAsString(product))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotImplemented());

        verify(adminService, times(1)).addProduct(any(Product.class), eq("ADMIN"));
    }
}