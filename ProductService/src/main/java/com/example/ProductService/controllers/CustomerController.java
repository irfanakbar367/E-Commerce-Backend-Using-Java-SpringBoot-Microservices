package com.example.ProductService.controllers;
import com.example.ProductService.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/products")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/getProductByName")
    public ResponseEntity<?> getProductByNameAndUserRole(@RequestParam String productName){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userRole = request.getHeader("X-User-Role");
        return customerService.getProductByNameAndUserRole(productName, userRole);
    }
    @GetMapping("/getAllProducts")
    public ResponseEntity<?> getAllProducts(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userRole = request.getHeader("X-User-Role");
        return customerService.getAllProducts(userRole);
    }
}
