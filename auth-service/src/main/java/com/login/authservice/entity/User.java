package com.login.authservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    private String browserId;
    private String otp;
    private Boolean otpVerified;

    // Explicit Getters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBrowserId() {
        return browserId;
    }

    public String getOtp() {
        return otp;
    }

    public Boolean getOtpVerified() {
        return otpVerified;
    }

    // Explicit Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBrowserId(String browserId) {
        this.browserId = browserId;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setOtpVerified(Boolean otpVerified) {
        this.otpVerified = otpVerified;
    }
}
