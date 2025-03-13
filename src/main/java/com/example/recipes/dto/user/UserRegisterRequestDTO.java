package com.example.recipes.dto.user;

import java.util.UUID;

public record UserRegisterRequestDTO(
        String name,
        String email,
        String password,
        String username
) {}

