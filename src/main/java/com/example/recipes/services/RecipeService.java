package com.example.recipes.services;

import com.example.recipes.domain.description.Description;
import com.example.recipes.domain.ingrediente.Ingrediente;
import com.example.recipes.domain.recipe.Recipe;
import com.example.recipes.domain.recipesList.RecipesList;
import com.example.recipes.domain.step.Step;
import com.example.recipes.dto.recipe.RecipeRequestDTO;
import com.example.recipes.dto.recipe.RecipeResponseDTO;
import com.example.recipes.dto.recipesList.RecipesListResponseDTO;
import com.example.recipes.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecipeService {
    private final IngredientRepository ingredientRepository;
    private final DescriptionRepository descriptionRepository;
    private final StepRepository stepRepository;
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
            // Salvando a receita (Recipe)
            RecipesList recipesList = recipesListService.getListObjById(recipeRequestDTO.listId());
            newRecipe.setName(recipeRequestDTO.name());
            newRecipe.setRecipesList(recipesList);
            newRecipe = recipesRepository.save(newRecipe);  // Salvando e atribuindo o ID gerado

            // Salvando a descrição (Description) com a referência à Recipe
            Description description = new Description();
            description.setRecipe(newRecipe);
            //description.setRecipe(newRecipe);  // Agora, newRecipe tem um ID
            descriptionRepository.save(description);

            // Salvando os ingredientes (Ingredientes) com a referência à Recipe
            List<Ingrediente> ingredients = recipeRequestDTO.ingredients();
            for (Ingrediente ingredient : ingredients) {
                ingredient.setRecipe(newRecipe);
            }
            ingredients = ingredientRepository.saveAll(ingredients);

            // Salvando os passos (Steps) com a referência à Recipe
            List<Step> steps = recipeRequestDTO.steps();
            for (Step step : steps) {
                step.setRecipe(newRecipe);
            }
            steps = stepRepository.saveAll(steps);

            // Associando as listas na Recipe
            newRecipe.setIngredientesList(ingredients);
            newRecipe.setStepsList(steps);
            newRecipe.setDescription(description);

            // Salvando a receita com todos os dados associados
            recipesRepository.save(newRecipe);

            return new RecipeResponseDTO(
                    newRecipe.getId(),
                    newRecipe.getName(),
                    ingredients,
                    steps,
                    description,
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
