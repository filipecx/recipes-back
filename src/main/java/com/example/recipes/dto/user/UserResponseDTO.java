package com.example.recipes.dto.user;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String nome,
        String email,
        String username
) {
}
