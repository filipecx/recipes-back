package com.example.recipes.services;

import com.example.recipes.domain.recipesList.RecipesList;
import com.example.recipes.dto.recipe.RecipeResponseDTO;
import com.example.recipes.dto.recipesList.RecipesListResponseDTO;
import com.example.recipes.repository.RecipesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecipeService {
    private final RecipesRepository recipesRepository;
    private final RecipesListService recipesListService;

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

    public List<RecipeResponseDTO> getRecipesByListId(UUID listId) {
        try {
            RecipesListResponseDTO recipesListResponseDTO = recipesListService.getListById(listId);
            return this.recipesRepository.findAllByList_Id(listId).stream()
                    .map(recipe -> new RecipeResponseDTO(
                            recipe.getId(),
                            recipe.getName(),
                            recipe.getIngredientesList(),
                            recipe.getStepsList(),
                            recipe.getDescription(),
                            recipe.getRecipesList() != null? recipe.getRecipesList().getId() : null
                    ))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new RuntimeException(e + "Unable to get recipes by id: " + listId);
        }


    }
}
