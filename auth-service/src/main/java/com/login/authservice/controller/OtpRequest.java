package com.login.authservice.controller;

/**
 * DTO representing OTP verification request payload.
 */
public class OtpRequest {
    private String email;
    private String otp;

    public OtpRequest() {
        // default constructor
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}