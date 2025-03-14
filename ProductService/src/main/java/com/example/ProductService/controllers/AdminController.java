package com.example.ProductService.controllers;

import com.example.ProductService.Models.Product;
import com.example.ProductService.services.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/products")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userRole = request.getHeader("X-User-Role");

        return adminService.addProduct(product, userRole);
    }
}
