package com.esprit.ftn.dto;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String email;
    private String password;
    // Optionnel: username si tu l’ajoutes à l’entité User
}
