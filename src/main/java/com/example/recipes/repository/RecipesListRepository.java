package com.example.recipes.repository;

import com.example.recipes.domain.recipesList.RecipesList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecipesListRepository extends JpaRepository<RecipesList, UUID> {

    List<RecipesList> findByUser_Id(UUID userId);
}
