package com.example.ProductService.services;

import com.example.ProductService.Models.Product;
import com.example.ProductService.repos.ProductRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private ProductRepo productRepo;
    @InjectMocks
    private AdminService adminService;
    private Product product;

    @BeforeEach
    void setUp(){
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
    void addProduct_shouldAddProductSuccessfully(){
        when(productRepo.save(any(Product.class))).thenReturn(product);

        ResponseEntity<Product> product1 = (ResponseEntity<Product>) adminService.addProduct(product, "ADMIN");

        assertEquals(product1.getStatusCode().value(), 200);
        assertEquals(product1.getBody().getName(), product.getName());
        assertEquals(product1.getBody().getPrice(), product.getPrice());
        Mockito.verify(productRepo, Mockito.times(1)).save(product);
    }

    @Test
    void addProduct_should_return_forbidden_if_NotAdmin(){

        ResponseEntity<Product> product1 = (ResponseEntity<Product>) adminService.addProduct(product, "USER");

        assertEquals(product1.getStatusCode().value(), 403);
        Mockito.verify(productRepo, Mockito.times(0)).save(product);
    }

    @Test
    void addProduct_shouldHandleException(){
        when(productRepo.save(any(Product.class))).thenThrow(new RuntimeException("Product is not saved. Please try again"));

        ResponseEntity<?> product1 = adminService.addProduct(product, "ADMIN");

        assertEquals(product1.getStatusCode().value(), 501);
        Mockito.verify(productRepo, Mockito.times(1)).save(product);
    }

}