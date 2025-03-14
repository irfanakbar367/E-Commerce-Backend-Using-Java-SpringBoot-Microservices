package com.example.OrderService.controllers;

import com.example.OrderService.models.Orders;
import com.example.OrderService.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/orders")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<?> placeOrder(@RequestBody Orders order) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String userId = request.getHeader(("X-User-Id"));
        String userRole = request.getHeader("X-User-Role");

        order.setUserId(Integer.parseInt(userId));

        return customerService.placeOrder(order, userRole);
    }

    @GetMapping("/seeMyOrders")
    public ResponseEntity<?> seeMyOrders(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int userId = Integer.parseInt(request.getHeader(("X-User-Id")));
        return customerService.seeMyOrders(userId);
    }
}