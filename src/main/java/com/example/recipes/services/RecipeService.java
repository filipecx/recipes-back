package com.example.recipes.services;

import com.example.recipes.dto.recipe.RecipeResponseDTO;
import com.example.recipes.dto.recipesList.RecipesListResponseDTO;
import com.example.recipes.repository.RecipesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecipeService {
    private final RecipesRepository recipesRepository;

    public List<RecipeResponseDTO> getAllRecipes() {
        return this.recipesRepository.findAll().stream()
                .map(recipe -> new RecipeResponseDTO(
                        recipe.getId(),
                        recipe.getName(),
                        recipe.getIngredientesList(),
                        recipe.getStepsList(),
                        recipe.getDescription(),
                        recipe.getRecipesList() != null? recipe.getRecipesList().getId() : null
                ))
                .collect(Collectors.toList());
    }


}
