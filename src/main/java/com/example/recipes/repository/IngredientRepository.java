package com.example.recipes.repository;

import com.example.recipes.domain.ingrediente.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IngredientRepository extends JpaRepository<Ingrediente, UUID> {
    List<Ingrediente> findAllByRecipe_Id(UUID recipeId);
}
