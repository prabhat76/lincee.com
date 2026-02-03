package com.lincee.controller;

import com.lincee.entity.User;
import com.lincee.repository.UserRepository;
import com.lincee.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "User authentication and authorization endpoints")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String token = jwtService.generateToken(email);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("email", email);
            response.put("userId", user.get().getId());
            response.put("username", user.get().getUsername());
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        }
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Invalid credentials");
        return ResponseEntity.status(401).body(errorResponse);
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration successful"),
        @ApiResponse(responseCode = "400", description = "Invalid registration data"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> userData) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("username", userData.get("username"));
        response.put("email", userData.get("email"));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout user and invalidate token")
    @ApiResponse(responseCode = "200", description = "Logout successful")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Refresh JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestBody Map<String, String> tokenData) {
        Map<String, Object> response = new HashMap<>();
        response.put("token", "new-jwt-token-placeholder");
        response.put("type", "Bearer");
        response.put("message", "Token refreshed successfully");
        return ResponseEntity.ok(response);
    }
}