package com.example.UserService.controllers;

import com.example.UserService.SecurityConfig.MyUserDetailsService;
import com.example.UserService.entities.Users;
import com.example.UserService.repositories.UserRepo;
import com.example.UserService.services.JwtService;
import com.example.UserService.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    JwtService jwtService;
    @Autowired
    ApplicationContext context;
    @Autowired
    UserRepo userRepo;

    @PostMapping("/addUser")
    public String addUser(@RequestBody Users users){
        return userService.addUser(users);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users users){
        return userService.login(users);
    }

    @GetMapping("/validateToken")
    public Users validateToken(@RequestParam String token){
        String username = jwtService.extractUserName(token);
        UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
        if (jwtService.validateToken(token, userDetails)){
            return userRepo.findByUsername(username);
        }
        return null;
    }

    @GetMapping("/getUserById")
    public ResponseEntity<?> getUserById(@RequestParam int userId){
        return userService.getUserById(userId);
    }

}
