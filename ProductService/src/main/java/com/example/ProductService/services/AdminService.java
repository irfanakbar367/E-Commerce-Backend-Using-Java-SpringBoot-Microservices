package com.example.ProductService.services;

import com.example.ProductService.Models.Product;
import com.example.ProductService.config.UserServiceClient;
import com.example.ProductService.dtos.UserDTO;
import com.example.ProductService.repos.ProductRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AdminService {
    private final ProductRepo productRepo;

    public AdminService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public ResponseEntity<?> addProduct(Product product, String userRole) {
        if (!"ADMIN".equals(userRole)) {
            return ResponseEntity.status(403).body("Access Denied: Only ADMIN can add products.");
        }
        try {
            product.setCreatedAt(LocalDateTime.now().toString());
            product.setUpdatedAt(LocalDateTime.now().toString());
            return ResponseEntity.ok(productRepo.save(product));
        }
        catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Product is not saved. Please try again");
        }
    }
}
