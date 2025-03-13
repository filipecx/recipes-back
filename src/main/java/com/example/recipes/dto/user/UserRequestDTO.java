package com.example.recipes.dto.user;

import java.util.UUID;

public record UserRequestDTO(
        UUID id,
        String username,
        String email
) {
}
