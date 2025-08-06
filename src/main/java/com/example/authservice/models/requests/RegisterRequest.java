package com.example.authservice.models.requests;

import com.example.authservice.models.ERole;

public record RegisterRequest(
        String username,
        String email,
        String password,
        String gender,
        String name,
        String phoneNumber,
        ERole role
) {
}
