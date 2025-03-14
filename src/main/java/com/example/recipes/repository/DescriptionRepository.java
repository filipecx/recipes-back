package com.example.recipes.repository;

import com.example.recipes.domain.description.Description;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DescriptionRepository extends JpaRepository<Description, UUID> {
}
