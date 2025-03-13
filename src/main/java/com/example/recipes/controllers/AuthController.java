package com.example.recipes.controllers;

import com.example.recipes.domain.user.User;
import com.example.recipes.dto.user.UserRegisterRequestDTO;
import com.example.recipes.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            return ResponseEntity.ok("Success!");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Fail!");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {

        try {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already being used");

        } catch (RuntimeException e) {
            userService.registerUser(userRegisterRequestDTO);

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRegisterRequestDTO.username(), userRegisterRequestDTO.password())
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            return ResponseEntity.status(HttpStatus.CREATED).body("Success!");
        }
    }
}
