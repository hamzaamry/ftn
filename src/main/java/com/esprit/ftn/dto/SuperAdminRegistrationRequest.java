package com.esprit.ftn.dto;

import lombok.Data;

@Data
public class SuperAdminRegistrationRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    //backup changes !! dummy comment

    public void setPassword(String password) {
        this.password = password;
    }
}