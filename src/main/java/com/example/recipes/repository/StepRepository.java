package com.example.recipes.repository;

import com.example.recipes.domain.step.Step;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StepRepository extends JpaRepository<Step, UUID> {
    List<Step> findAllByRecipe_Id(UUID recipeId);
}
