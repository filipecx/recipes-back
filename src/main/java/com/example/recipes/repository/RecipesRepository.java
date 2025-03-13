package com.example.recipes.repository;

import com.example.recipes.domain.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecipesRepository extends JpaRepository<Recipe, UUID> {
    List<Recipe> findAllByList_Id(UUID listId);
}
