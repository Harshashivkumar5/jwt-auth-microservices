package com.login.authservice.service;

import com.login.authservice.entity.User;
import com.login.authservice.repository.UserRepository;
import com.login.authservice.security.JwtUtil;
import com.login.authservice.controller.AuthRequest;
import com.login.authservice.dto.OtpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Password validation regex
    private static final String PASSWORD_REGEX = 
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$";

    private static final String PASSWORD_RULES = 
        "Password must be at least 8 characters with 1 uppercase, 1 lowercase, 1 number, and 1 special character";

    /**
     * Register a new user
     */
    public Map<String, Object> register(User user) {
        Map<String, Object> response = new HashMap<>();

        // Validate email
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Email is required");
            return response;
        }

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            response.put("success", false);
            response.put("message", "Email already exists");
            logger.warn("Attempted registration with duplicate email: {}", user.getEmail());
            return response;
        }

        // Validate password
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Password is required");
            return response;
        }

        if (!user.getPassword().matches(PASSWORD_REGEX)) {
            response.put("success", false);
            response.put("message", PASSWORD_RULES);
            logger.warn("Weak password attempt for email: {}", user.getEmail());
            return response;
        }

        // Save user
        try {
            // Encode password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            response.put("success", true);
            response.put("message", "User Registered Successfully");
            logger.info("User registered successfully: {}", user.getEmail());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            logger.error("Error during registration: {}", e.getMessage());
        }

        return response;
    }

    /**
     * Login user and generate JWT token
     */
    public Map<String, Object> login(String email, String password) {
        Map<String, Object> response = new HashMap<>();

        // Validate input
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Email and password are required");
            return response;
        }

        // Find user by email
        User user = userRepository.findByEmail(email)
            .orElse(null);

        if (user == null) {
            response.put("success", false);
            response.put("message", "Invalid credentials");
            logger.warn("Login attempt with non-existent email: {}", email);
            return response;
        }

        // Verify password using encoder
        if (!passwordEncoder.matches(password, user.getPassword())) {
            response.put("success", false);
            response.put("message", "Invalid credentials");
            logger.warn("Failed login attempt for email: {}", email);
            return response;
        }

        // Generate JWT token
        try {
            String token = jwtUtil.generateToken(email);
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("token", token);
            logger.info("User logged in successfully: {}", email);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Token generation failed: " + e.getMessage());
            logger.error("Error generating token: {}", e.getMessage());
        }

        return response;
    }

    /**
     * Process login with browser tracking and optional OTP requirement
     */
    public ResponseEntity<?> processLogin(
            AuthRequest request,
            String userAgent) {

        User user =
                userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid Password");
        }

        // Handle null userAgent
        if (userAgent == null) {
            userAgent = "unknown-client";
        }

        String browserHash =
                DigestUtils.md5DigestAsHex(
                        userAgent.getBytes());

        // First login
        if (user.getBrowserId() == null) {
            user.setBrowserId(browserHash);
            userRepository.save(user);

            try {
                String token = jwtUtil.generateToken(user.getEmail());
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("token", token);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                logger.error("Error generating token: {}", e.getMessage());
                throw new RuntimeException("Token generation failed");
            }
        }

        // New browser detected
        if (!user.getBrowserId().equals(browserHash)) {
            String otp = "123456"; // Dummy OTP
            user.setOtp(otp);
            user.setOtpVerified(false);
            userRepository.save(user);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("OTP_REQUIRED");
        }

        // Same browser, existing user
        try {
            String token = jwtUtil.generateToken(user.getEmail());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error generating token: {}", e.getMessage());
            throw new RuntimeException("Token generation failed");
        }
    }

    /**
     * Check OTP provided by user and issue JWT if correct
     */
    public ResponseEntity<?> verifyOtp(OtpRequest request) {
        User user =
                userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() != null &&
            user.getOtp().equals(request.getOtp())) {

            user.setOtpVerified(true);
            userRepository.save(user);

            return ResponseEntity.ok(
                    jwtUtil.generateToken(user.getEmail()));
        }

        return ResponseEntity
                .badRequest()
                .body("Invalid OTP");
    }
}
