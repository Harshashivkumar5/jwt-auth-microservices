package com.login.authservice.dto;

/**
 * DTO representing OTP verification request payload.
 */
public class OtpRequest {

    private String email;
    private String otp;

    public OtpRequest() {
        // default constructor
    }

    public OtpRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
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