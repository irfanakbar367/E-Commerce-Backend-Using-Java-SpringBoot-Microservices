package com.example.ProductService.services;

import com.example.ProductService.Models.Product;
import com.example.ProductService.dtos.ProductDTO;
import com.example.ProductService.repos.ProductRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final ProductRepo productRepo;

    public CustomerService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public ResponseEntity<?> getProductUserByName(String productName, String userRole) {
        if (!"USER".equals(userRole)) {
            return ResponseEntity.status(403).body("Access Denied: Only Customer can see products.");
        }
        Product product = productRepo.findByName(productName);
        if (product != null) {
            ProductDTO productDTO = new ProductDTO();

            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setStock(product.getStock());
            productDTO.setCategory(product.getCategory());
            productDTO.setSubCategory(product.getSubCategory());
            productDTO.setImages(product.getImages());

            return ResponseEntity.ok(productDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with name : " + productName + " is not found");
    }

    public ResponseEntity<?> getAllProducts(String userRole) {
        if (!"USER".equals(userRole)) {
            return ResponseEntity.status(403).body("Access Denied: Only Customer can see products.");
        }
        List<Product> productList = productRepo.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();
        if (productList != null && !productList.isEmpty()) {
            for (Product product : productList) {
                ProductDTO productDTO = new ProductDTO();

                productDTO.setId(product.getId());
                productDTO.setName(product.getName());
                productDTO.setDescription(product.getDescription());
                productDTO.setPrice(product.getPrice());
                productDTO.setStock(product.getStock());
                productDTO.setCategory(product.getCategory());
                productDTO.setSubCategory(product.getSubCategory());
                productDTO.setImages(product.getImages());

                productDTOS.add(productDTO);
            }
            return ResponseEntity.ok(productDTOS);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Product found");

    }
}
