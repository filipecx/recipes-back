package com.example.recipes.controllers;

import com.example.recipes.dto.recipe.RecipeRequestDTO;
import com.example.recipes.dto.recipe.RecipeResponseDTO;
import com.example.recipes.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<List<RecipeResponseDTO>> getAllRecipes() {
        List<RecipeResponseDTO> recipeResponseDTOS = recipeService.getAllRecipes();

        if (recipeResponseDTOS.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No recipes found");
        }
        return ResponseEntity.ok().body(recipeResponseDTOS);
    }

    @GetMapping("/{recipeListId}")
    public ResponseEntity<List<RecipeResponseDTO>> getRecipesByListId(@PathVariable UUID recipeListId) {
        List<RecipeResponseDTO> recipeResponseDTOS = recipeService.getRecipesByListId(recipeListId);

        if (recipeResponseDTOS.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No recipes found");
        }
        return ResponseEntity.ok().body(recipeResponseDTOS);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponseDTO> getRecipeById(@PathVariable UUID recipeId) {
        try {
            RecipeResponseDTO recipeResponseDTO = recipeService.getRecipeById(recipeId);
            return ResponseEntity.ok().body(recipeResponseDTO);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No recipes found");
        }
    }

    @PostMapping
    public ResponseEntity<RecipeResponseDTO> addRecipe(@RequestBody RecipeRequestDTO recipeRequestDTO) {
        try {
            RecipeResponseDTO recipeResponseDTO = recipeService.addRecipe(recipeRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(recipeResponseDTO);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipe not created");
        }
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<RecipeResponseDTO> updateRecipe(@RequestBody RecipeRequestDTO recipeRequestDTO, @PathVariable UUID recipeId) {
        try {
            RecipeResponseDTO recipeResponseDTO = recipeService.updateRecipe(recipeRequestDTO, recipeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(recipeResponseDTO);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipe not updated");
        }
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<?> deleteRecipe(@PathVariable UUID recipeId) {
        try {
            recipeService.deleteRecipe(recipeId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipe not deleted");
        }
    }
}
