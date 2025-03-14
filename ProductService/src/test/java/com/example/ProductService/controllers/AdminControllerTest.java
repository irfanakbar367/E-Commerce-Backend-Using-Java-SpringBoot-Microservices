package com.example.ProductService.controllers;

import com.example.ProductService.Models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(AdminController.class)
class AdminControllerTest {


    @Test
    public void testAddProduct() throws Exception {
        Product product = new Product();

    }
}