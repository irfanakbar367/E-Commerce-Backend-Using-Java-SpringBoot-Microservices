package com.example.ProductService.controllers;

import com.example.ProductService.Models.Product;
import com.example.ProductService.dtos.ProductDTO;
import com.example.ProductService.services.CustomerService;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void getProductByNameAndUserRole_shouldReturn_Ok_WhenProductFound() throws Exception {
        String productName = "product1";

        when(customerService.getProductByNameAndUserRole(eq(productName), eq("USER")))
                .thenReturn((ResponseEntity) ResponseEntity.ok("Product Found"));

        mockMvc.perform(get("/products/getProductByName")
                        .header("X-User-Role", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("productName", productName))
                .andExpect(status().isOk());

        verify(customerService, times(1)).getProductByNameAndUserRole(
                eq(productName), eq("USER"));

    }

    @Test
    void getProductByNameAndUserRole_shouldReturn_Ok_WhenNotUSER() throws Exception {
        String productName = "product1";

        when(customerService.getProductByNameAndUserRole(eq(productName), eq("ADMIN")))
                .thenReturn((ResponseEntity) ResponseEntity.status(403).body
                        ("Access Denied: Only Customer can see products."));

        mockMvc.perform(get("/products/getProductByName")
                        .header("X-User-Role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("productName", productName))
                .andExpect(status().isForbidden());

        verify(customerService, times(1)).getProductByNameAndUserRole(
                eq(productName), eq("ADMIN"));

    }

    @Test
    void getProductByNameAndUserRole_shouldReturn_Ok_WhenProductNotFound() throws Exception {
        String productName = "product1";

        when(customerService.getProductByNameAndUserRole(eq(productName), eq("USER")))
                .thenReturn((ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND).body(""));

        mockMvc.perform(get("/products/getProductByName")
                        .header("X-User-Role", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("productName", productName))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).getProductByNameAndUserRole(
                eq(productName), eq("USER"));

    }

    @Test
    void getAllProducts_shouldReturn_Ok_WhenProductsFound() throws Exception {

        List<ProductDTO> productDTOS = new ArrayList<>();
        for (int i=0; i<3; i++) {
            ProductDTO productDTO = new ProductDTO();

            productDTO.setId(i);
            productDTO.setName("product" + i);
            productDTO.setDescription("product" + i + " Description");
            productDTO.setPrice(BigDecimal.valueOf(i));
            productDTO.setStock(i);
            productDTO.setCategory("product" + i + " Category");
            productDTO.setSubCategory("product" + i + " SubCategory");
            productDTO.setImages(Collections.singletonList("product" + i + " Images"));

            productDTOS.add(productDTO);
        }

        when(customerService.getAllProducts(eq("USER")))
                .thenReturn((ResponseEntity) ResponseEntity.ok(productDTOS));

        mockMvc.perform(get("/products/getAllProducts")
                        .header("X-User-Role", "USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(0))
                .andExpect(jsonPath("$[0].name").value("product0"))
                .andExpect(jsonPath("$[0].description").value("product0 Description"))
                .andExpect(jsonPath("$[0].price").value(0))
                .andExpect(jsonPath("$[0].stock").value(0))
                .andExpect(jsonPath("$[0].category").value("product0 Category"))
                .andExpect(jsonPath("$[0].subCategory").value("product0 SubCategory"))
                .andExpect(jsonPath("$[0].images[0]").value("product0 Images"));

        verify(customerService, times(1)).getAllProducts(eq("USER"));

    }

    @Test
    void getAllProducts_shouldReturn_Ok_WhenNotUSER() throws Exception {

        when(customerService.getAllProducts(eq("ADMIN")))
                .thenReturn((ResponseEntity) ResponseEntity.status(403).body
                        ("Access Denied: Only Customer can see products."));

        mockMvc.perform(get("/products/getAllProducts")
                        .header("X-User-Role", "ADMIN"))
                .andExpect(status().isForbidden());

        verify(customerService, times(1)).getAllProducts(eq("ADMIN"));

    }

    @Test
    void getAllProducts_shouldReturn_Ok_WhenProductsNotFound() throws Exception {

        when(customerService.getAllProducts(eq("USER")))
                .thenReturn((ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND).body(""));

        mockMvc.perform(get("/products/getAllProducts")
                        .header("X-User-Role", "USER"))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).getAllProducts(
                eq("USER"));

    }
}