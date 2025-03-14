package com.example.ProductService.repos;

import com.example.ProductService.Models.Product;
import com.example.ProductService.dtos.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    Product findByName(String productName);
}
