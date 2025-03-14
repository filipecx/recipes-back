package com.example.recipes.domain.description;

import com.example.recipes.domain.recipe.Recipe;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "descriptions")
@Table(name = "descriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Description {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String text;

    private String makes;

    private String time;

    @OneToOne
    @JoinColumn(name = "recipe_id")
    @JsonBackReference
    private Recipe recipe;
}
