package com.proyecto1.proyecto1progra4.Data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public record LoginRequest(
            @NotBlank @Email String username,
            @NotBlank @Size(min = 3, max = 100) String clave
    ) {}

    public record LoginResponse(String token, String username, String role) {}
}
