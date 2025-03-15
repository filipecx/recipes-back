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
        List<Recipe> recipeList = recipesRepository.findAll();
        return recipeList.stream().map(
                recipe -> this.getRecipeById(recipe.getId())
        ).toList();
    }

    public List<RecipeResponseDTO> getRecipesByListId(UUID id) {
            RecipesListResponseDTO recipesListResponseDTO = recipesListService.getListById(id);
            List<Recipe> recipeList = recipesRepository.findAllByRecipesList_Id(id);

            return recipeList.stream().map(
                    recipe -> this.getRecipeById(recipe.getId())
            ).toList();
    }

    public RecipeResponseDTO getRecipeById(UUID id) {
        Recipe recipe = recipesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found by id: " + id));
        Description description = descriptionRepository.findByRecipe_Id(id);
        List<Step> stepList = stepRepository.findAllByRecipe_Id(id);
        List<Ingrediente> ingredienteList = ingredientRepository.findAllByRecipe_Id(id);
        return new RecipeResponseDTO(
                recipe.getId(),
                recipe.getName(),
                ingredienteList,
                stepList,
                description,
                recipe.getRecipesList().getId()
        );
    }

    public RecipeResponseDTO addRecipe(RecipeRequestDTO recipeRequestDTO) {
        RecipesList recipesList = recipesListService.getListObjById(recipeRequestDTO.listId());

        Recipe newRecipe = new Recipe();
        newRecipe.setRecipesList(recipesList);
        newRecipe.setName(recipeRequestDTO.name());
        Recipe savedRecipe = recipesRepository.save(newRecipe);

        Description description = new Description();
        description.setRecipe(savedRecipe);
        description.setTime(recipeRequestDTO.description().getTime());
        description.setText(recipeRequestDTO.description().getText());
        description.setMakes(recipeRequestDTO.description().getMakes());
        descriptionRepository.save(description);

        List<Ingrediente> ingredienteList = recipeRequestDTO.ingredients()
                .stream()
                .map(ingrediente -> {
                    Ingrediente ingredient = new Ingrediente();
                    ingredient.setRecipe(savedRecipe);
                    ingredient.setName(ingrediente.getName());
                    ingredient.setQuantity(ingrediente.getQuantity());
                    return ingredient;
                })
                .collect(Collectors.toList());
        ingredientRepository.saveAll(ingredienteList);

        List<Step> setpList = recipeRequestDTO.steps()
                .stream()
                .map(step -> {
                    Step newStep = new Step();
                    newStep.setRecipe(newRecipe);
                    newStep.setText(step.getText());
                    newStep.setNumber(step.getNumber());
                    return newStep;
                })
                .collect(Collectors.toList());
        stepRepository.saveAll(setpList);

        return new RecipeResponseDTO(
                newRecipe.getId(),
                newRecipe.getName(),
                ingredienteList,
                setpList,
                description,
                newRecipe.getRecipesList().getId()
        );
    }
    public RecipeResponseDTO updateRecipe(RecipeRequestDTO recipeRequestDTO, UUID id) {
        try {
            Recipe recipe = recipesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Recipe not found by id: " + id));

            Description description = descriptionRepository.findByRecipe_Id(id);
            List<Step> stepList = stepRepository.findAllByRecipe_Id(id);
            List<Ingrediente> ingredienteList = ingredientRepository.findAllByRecipe_Id(id);

            recipe.setName(recipeRequestDTO.name());

            stepList.clear();
            stepList.addAll(recipeRequestDTO.steps());
            stepList.forEach(step -> {
                step.setRecipe(recipe);
                step.setText(step.getText());
                step.setNumber(step.getNumber());
            });
            stepRepository.saveAll(stepList);

            ingredienteList.clear();
            ingredienteList.addAll(recipeRequestDTO.ingredients());
            ingredienteList.forEach(ingrediente -> ingrediente.setRecipe(recipe));
            ingredientRepository.saveAll(ingredienteList);

            description.setRecipe(recipe);
            description.setText(recipeRequestDTO.description().getText());
            description.setTime(recipeRequestDTO.description().getTime());
            description.setMakes(recipeRequestDTO.description().getMakes());
            descriptionRepository.save(description);

            return new RecipeResponseDTO(
                    recipe.getId(),
                    recipe.getName(),
                    ingredienteList,
                    stepList,
                    description,
                    recipe.getRecipesList().getId()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error updating recipe", e);
        }
    }


    public void deleteRecipe(UUID id) {
        Recipe recipe = recipesRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Recipe not found by id: " + id));

        Description description = descriptionRepository.findByRecipe_Id(id);
        descriptionRepository.delete(description);

        List<Step> stepList = stepRepository.findAllByRecipe_Id(id);
        stepRepository.deleteAll(stepList);

        List<Ingrediente> ingredienteList = ingredientRepository.findAllByRecipe_Id(id);
        ingredientRepository.deleteAll(ingredienteList);

        recipesRepository.delete(recipe);
    }
}
