package com.login.authservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import com.login.authservice.entity.User;
import com.login.authservice.service.AuthService;
import com.login.authservice.controller.AuthRequest;
import com.login.authservice.dto.OtpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Register API
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }

    // Login API
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthRequest request,
            HttpServletRequest httpRequest) {

        try {
            String userAgent = httpRequest.getHeader("User-Agent");
            System.out.println("Login endpoint called, UA=" + userAgent);
            ResponseEntity<?> resp = authService.processLogin(request, userAgent);
            return resp;
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }
    }

    // OTP Verification
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestBody OtpRequest request) {

        return authService.verifyOtp(request);
    }
}