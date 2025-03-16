package com.example.ProductService.services;

import com.example.ProductService.Models.Product;
import com.example.ProductService.dtos.ProductDTO;
import com.example.ProductService.repos.ProductRepo;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private ProductRepo productRepo;
    @InjectMocks
    private CustomerService customerService;

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
    }

    @Test
    void getProductByNameAndUserRole_should_return_forbidden_if_NotUSER() {
        String productName = "product1";

        ResponseEntity<?> productFound = customerService.getProductByNameAndUserRole(productName, "ADMIN");

        assertEquals(productFound.getStatusCode().value(), 403);
        verify(productRepo, Mockito.times(0)).findByName(productName);
    }

    @Test
    void getProductByNameAndUserRole_should_return_product_if_Found() {
        String productName = "Laptop";

        when(productRepo.findByName(productName)).thenReturn(product);

        ResponseEntity<ProductDTO> productFound = (ResponseEntity<ProductDTO>) customerService.getProductByNameAndUserRole(productName, "USER");

        assertEquals(productFound.getStatusCode().value(), 200);
        assertNotNull(productFound.getBody());
        assertEquals(productFound.getBody().getName(), product.getName());
        assertEquals(productFound.getBody().getPrice(), product.getPrice());

        verify(productRepo, Mockito.times(1)).findByName(productName);
    }

    @Test
    void getProductByNameAndUserRole_should_return_Message_if_NotFound() {
        String productName = "product1";

        when(productRepo.findByName(productName)).thenReturn(null);

        ResponseEntity<?> productFound = customerService.getProductByNameAndUserRole(productName, "USER");

        assertEquals(productFound.getStatusCode().value(), 404);
        verify(productRepo, Mockito.times(1)).findByName(productName);
    }

    @Test
    void getAllProducts_should_return_forbidden_if_NotUSER() {

        ResponseEntity<?> productFound = customerService.getAllProducts("ADMIN");

        assertEquals(productFound.getStatusCode().value(), 403);
        verify(productRepo, Mockito.times(0)).findAll();
    }

    @Test
    void getAllProducts_should_return_Message_if_NotFound() {

        when(productRepo.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<?> productFound = customerService.getAllProducts("USER");

        assertEquals(productFound.getStatusCode().value(), 404);
        verify(productRepo, Mockito.times(1)).findAll();
    }

    @Test
    void getAllProducts_should_return_product_if_Found() {

        List<Product> productDTOList = new LinkedList<>();
        Product productDTO = new Product();
        productDTO.setName("Laptop");
        productDTO.setDescription("High-performance laptop");
        productDTO.setPrice(BigDecimal.valueOf(999.99));
        productDTO.setStock(50);
        productDTO.setCategory("Electronics");
        productDTO.setSubCategory("Computers");
        productDTOList.add(productDTO);

        when(productRepo.findAll()).thenReturn(productDTOList);

        ResponseEntity<List<ProductDTO>> productFound = (ResponseEntity<List<ProductDTO>>) customerService.getAllProducts("USER");

        assertEquals(productFound.getStatusCode().value(), 200);
        assertNotNull(productFound.getBody());
        assertEquals(productFound.getBody().size(), 1);
        assertEquals(productFound.getBody().get(0).getName(), productDTO.getName());
        assertEquals(productFound.getBody().get(0).getPrice(), productDTO.getPrice());

        verify(productRepo, Mockito.times(1)).findAll();
    }
}