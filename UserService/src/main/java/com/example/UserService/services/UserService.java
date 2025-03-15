package com.example.UserService.services;

import com.example.UserService.dtos.UserDTO;
import com.example.UserService.entities.Users;
import com.example.UserService.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    @Autowired
    private JwtService jwtService;

    public ResponseEntity<UserDTO> getUserById(int userId ){
        Users user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setName(user.getUsername());
            userDTO.setRole(user.getRole());
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    public String addUser(Users users) {
        try{
            users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
            userRepo.save(users);
            return "User Saved";
        }
        catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            return "User save failed ";
        }
    }

    public String login(Users users) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));
        if (authentication.isAuthenticated())
            return jwtService.generateToken(users.getUsername());
        return "User Not Found.";
    }
}
