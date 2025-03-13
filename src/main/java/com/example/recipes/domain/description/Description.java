package com.example.recipes.domain.description;

import com.example.recipes.domain.recipe.Recipe;
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

    private Integer makes;

    private String time;

    @OneToOne
    @JoinColumn(name = "recipe_id", nullable = false, unique = true)
    private Recipe recipe;
}
