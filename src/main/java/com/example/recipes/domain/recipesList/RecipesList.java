package com.example.recipes.domain.recipesList;

import com.example.recipes.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "recipesLists")
@Table(name = "recipesLists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipesList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
