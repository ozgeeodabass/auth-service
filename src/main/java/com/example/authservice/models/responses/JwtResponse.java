package com.example.authservice.models.responses;

import com.example.authservice.models.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private UUID id;
    private String username;
    private String email;
    private String gender;
    private String name;
    private String phoneNumber;
    private ERole role;
}
