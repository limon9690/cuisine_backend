package org.limon.cuisine_backend.recipe;

import org.limon.cuisine_backend.customException.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    public List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
    }

    public Optional<Recipe> getRecipeByName(String name) {
        return recipeRepository.findByName(name);
    }

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public void updateRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    public void deleteRecipeById(Long id) {
        recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
        recipeRepository.deleteById(id);
    }
}
