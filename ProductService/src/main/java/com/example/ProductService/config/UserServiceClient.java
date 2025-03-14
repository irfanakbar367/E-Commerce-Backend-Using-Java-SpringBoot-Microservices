package com.example.ProductService.config;

import com.example.ProductService.dtos.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("USER-SERVICE")
public interface UserServiceClient {

    @GetMapping("/users/getUserById")
    UserDTO getUserById(@RequestParam int userId);

}
