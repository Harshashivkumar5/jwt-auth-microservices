package com.login.authservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import com.login.authservice.entity.User;
import com.login.authservice.service.AuthService;
import com.login.authservice.controller.AuthRequest;
import com.login.authservice.dto.OtpRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }

    /**
     * Login user and get JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody AuthRequest request,
        HttpServletRequest httpRequest) {

    String userAgent =
        httpRequest.getHeader("User-Agent");

    // 🔴 Block REST clients
    if(userAgent == null ||
       !userAgent.contains("Mozilla")) {

        return ResponseEntity
         .status(HttpStatus.FORBIDDEN)
         .body("Login not allowed from REST client");
    }

    return authService.processLogin(request, userAgent);
    }

    /**
     * Verify the one-time password sent to the user for a new browser
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestBody OtpRequest request) {
        return authService.verifyOtp(request);
    }
}

