package com.example.recipes.dto.recipe;

import com.example.recipes.domain.description.Description;
import com.example.recipes.domain.ingrediente.Ingrediente;
import com.example.recipes.domain.step.Step;

import java.util.List;
import java.util.UUID;

public record RecipeResponseDTO(
        UUID id,
        String name,
        List<Ingrediente> ingredients,
        List<Step> steps,
        Description description,
        UUID listId
) {
}
