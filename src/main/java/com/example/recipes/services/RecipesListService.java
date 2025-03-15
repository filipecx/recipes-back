package com.example.recipes.services;

import com.example.recipes.domain.recipesList.RecipesList;
import com.example.recipes.domain.user.User;
import com.example.recipes.dto.recipesList.RecipesListRequestDTO;
import com.example.recipes.dto.recipesList.RecipesListResponseDTO;
import com.example.recipes.dto.user.UserResponseDTO;
import com.example.recipes.repository.RecipesListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipesListService {
    private final RecipesListRepository recipesListRepository;
    private final UserService userService;

    public List<RecipesListResponseDTO> getAllLists() {
        return this.recipesListRepository.findAll().stream()
                .map(recipesList -> new RecipesListResponseDTO(
                        recipesList.getId(),
                        recipesList.getName(),
                        recipesList.getUser() != null? recipesList.getUser().getId() : null
                ))
                .collect(Collectors.toList());
    }

    public List<RecipesListResponseDTO> getListByUserId(UUID userId) {
        return this.recipesListRepository.findByUser_Id(userId).stream()
                .map(recipesList -> new RecipesListResponseDTO(
                        recipesList.getId(),
                        recipesList.getName(),
                        recipesList.getUser().getId()
                ))
                .collect(Collectors.toList());
    }

    public RecipesListResponseDTO getListById(UUID id) {
        RecipesList recipesList = this.recipesListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("List by id: " + id + "not found!"));

        return new RecipesListResponseDTO(
                recipesList.getId(),
                recipesList.getName(),
                recipesList.getUser().getId());
    }

    public RecipesList getListObjById(UUID id) {
        return recipesListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("List not found"));
    }

    public RecipesListResponseDTO addList(RecipesListRequestDTO recipesListRequestDTO, UUID userId) {
        RecipesList recipesList = new RecipesList();
        UserResponseDTO userResponseDTO = userService.getUserById(userId);

        User user = new User();
        user.setUsername(userResponseDTO.username());
        user.setName(userResponseDTO.nome());
        user.setId(userResponseDTO.id());
        user.setEmail(userResponseDTO.email());

        recipesList.setName(recipesListRequestDTO.name());
        recipesList.setUser(user);

        recipesListRepository.save(recipesList);

        return new RecipesListResponseDTO(
                recipesList.getId(),
                recipesList.getName(),
                recipesList.getUser().getId()
        );
    }

    public String deleteList(UUID id) {
        RecipesList recipesList = recipesListRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("List not found with id: " + id));

        recipesListRepository.delete(recipesList);

        return "List successfully removed";
    }

    public RecipesListResponseDTO updateList(RecipesListRequestDTO recipesListRequestDTO, UUID id) {
        RecipesList recipesList = recipesListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("List not found with id: " + id));

         recipesList.setName(recipesListRequestDTO.name());

         recipesListRepository.save(recipesList);
         return new RecipesListResponseDTO(
                 recipesList.getId(),
                 recipesList.getName(),
                 recipesList.getUser().getId()
         );

    }
}
