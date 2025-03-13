package com.example.recipes.dto.recipesList;

import java.util.UUID;

public record RecipesListResponseDTO(
        UUID id,
        String name,
        UUID userId
) {
}
