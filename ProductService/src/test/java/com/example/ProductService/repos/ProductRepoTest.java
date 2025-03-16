package com.example.ProductService.repos;

import com.example.ProductService.Models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepoTest {

    @Autowired
    private ProductRepo productRepo;

    @Test
    void shouldSaveAndFindProductByUsername() {
        Product product = new Product();
        product.setName("Laptop");
        product.setDescription("High-performance laptop");
        product.setPrice(BigDecimal.valueOf(999.99));
        product.setStock(50);
        product.setCategory("Electronics");
        product.setSubCategory("Computers");
        product.setCreatedAt(LocalDateTime.now().toString());
        product.setUpdatedAt(LocalDateTime.now().toString());

        Product productSaved = productRepo.save(product);
        assertNotNull(productSaved);
        assertEquals(productSaved.getName(), product.getName());

        Product productFound = productRepo.findByName("Laptop");
        assertNotNull(productFound);
        assertEquals(productFound.getName(), product.getName());
    }
}