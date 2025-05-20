package com.esprit.ftn.dto;

import lombok.Data;

@Data
public class UserRoleAssignmentRequest {
    private Long userId;
    private String role; // "COACH" or "ATHLETE"
}