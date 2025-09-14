package com.example.recipes.controllers;

import com.example.recipes.domain.user.User;
import com.example.recipes.dto.recipesList.RecipesListRequestDTO;
import com.example.recipes.dto.recipesList.RecipesListResponseDTO;
import com.example.recipes.dto.user.UserResponseDTO;
import com.example.recipes.services.RecipesListService;
import com.example.recipes.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recipeslist")
public class RecipesListController {
    private final RecipesListService recipesListService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<RecipesListResponseDTO>> getAllLists() {
        List<RecipesListResponseDTO> recipesLists = recipesListService.getAllLists();
        if (recipesLists.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No recipes list found");
        }

        return ResponseEntity.ok().body(recipesLists);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<RecipesListResponseDTO>> getListsByUserId(@PathVariable UUID userId) {
        List<RecipesListResponseDTO> recipesListResponseDTOS = recipesListService.getListByUserId(userId);
        if (recipesListResponseDTOS.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No recipes list found");
        }

        return ResponseEntity.ok().body(recipesListResponseDTOS);
    }

    @PostMapping
    public ResponseEntity<?> addList(@RequestBody RecipesListRequestDTO recipesListRequestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("dto: " + recipesListRequestDTO);
        try {
            User user = userService.getUserByUsername(userDetails.getUsername());

            RecipesListResponseDTO recipesListResponseDTO = recipesListService.addList(recipesListRequestDTO, user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(recipesListResponseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recipe not created");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateList(@RequestBody RecipesListRequestDTO recipesListRequestDTO, @PathVariable UUID id) {
        try {
            RecipesListResponseDTO recipesListResponseDTO = recipesListService.updateList(recipesListRequestDTO, id);
            return ResponseEntity.status(HttpStatus.CREATED).body(recipesListResponseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recipe List not updated");
        }
    }
}
