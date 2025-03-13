package com.example.recipes.services;

import com.example.recipes.domain.recipe.Recipe;
import com.example.recipes.domain.recipesList.RecipesList;
import com.example.recipes.dto.recipe.RecipeRequestDTO;
import com.example.recipes.dto.recipe.RecipeResponseDTO;
import com.example.recipes.dto.recipesList.RecipesListResponseDTO;
import com.example.recipes.repository.RecipesListRepository;
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
    private final RecipesListRepository recipesListRepository;
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
            return this.recipesRepository.findAllByRecipesList_Id(listId).stream()
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

    public RecipeResponseDTO getRecipeById(UUID id) {
        Recipe recipe = recipesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found by id: " + id));
        return new RecipeResponseDTO(
                recipe.getId(),
                recipe.getName(),
                recipe.getIngredientesList(),
                recipe.getStepsList(),
                recipe.getDescription(),
                recipe.getRecipesList().getId()
        );
    }

    public RecipeResponseDTO addRecipe(RecipeRequestDTO recipeRequestDTO) {
        Recipe newRecipe = new Recipe();
        try {

            RecipesList recipesList = recipesListService.getListObjById(recipeRequestDTO.listId());

            newRecipe.setName(recipeRequestDTO.name());
            newRecipe.setIngredientesList(recipeRequestDTO.ingredients());
            newRecipe.setStepsList(recipeRequestDTO.steps());
            newRecipe.setDescription(recipeRequestDTO.description());
            newRecipe.setRecipesList(recipesList);

            recipesRepository.save(newRecipe);

            return new RecipeResponseDTO(
                    newRecipe.getId(),
                    newRecipe.getName(),
                    newRecipe.getIngredientesList(),
                    newRecipe.getStepsList(),
                    newRecipe.getDescription(),
                    newRecipe.getRecipesList().getId()
            );
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    public RecipeResponseDTO updateRecipe(RecipeRequestDTO recipeRequestDTO, UUID id) {
        Recipe recipe = recipesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found by id: " + id));

        recipe.setName(recipeRequestDTO.name());
        recipe.setIngredientesList(recipeRequestDTO.ingredients());
        recipe.setStepsList(recipeRequestDTO.steps());
        recipe.setDescription(recipeRequestDTO.description());

        recipesRepository.save(recipe);

        return new RecipeResponseDTO(
                recipe.getId(),
                recipe.getName(),
                recipe.getIngredientesList(),
                recipe.getStepsList(),
                recipe.getDescription(),
                recipe.getRecipesList().getId()
        );
    }
    public void deleteRecipe(UUID id) {
        Recipe recipe = recipesRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Recipe not found by id: " + id));
        recipesRepository.delete(recipe);
    }
}
