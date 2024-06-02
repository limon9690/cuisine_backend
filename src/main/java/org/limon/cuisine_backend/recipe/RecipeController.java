package org.limon.cuisine_backend.recipe;

import org.limon.cuisine_backend.customException.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @GetMapping()
    public List<Recipe> getRecipes() {
        return recipeService.getRecipes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id) {
        try {
            Recipe foundRecipe = recipeService.getRecipeById(id);
            return ResponseEntity.ok(foundRecipe);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> addRecipe(@RequestBody Recipe recipe) {
        Optional<Recipe> ifExists = recipeService.getRecipeByName(recipe.getName());
        if (ifExists.isPresent()) return ResponseEntity.status(HttpStatus.CONFLICT).body("Recipe already exists");

        if (recipe.getName().isEmpty() || recipe.getDescription().isEmpty() || recipe.getIngredients().isEmpty() || recipe.getDirections().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Attributes can't be empty");
        } else {
            recipeService.createRecipe(recipe);
            return ResponseEntity.ok().body(Map.of("id", recipe.getId()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        if (recipe.getName().isEmpty() || recipe.getDescription().isEmpty() || recipe.getIngredients().isEmpty() || recipe.getDirections().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Attributes can't be empty");
        }

        try {
            Recipe foundRecipe = recipeService.getRecipeById(id);

            foundRecipe.setName(recipe.getName());
            foundRecipe.setDescription(recipe.getDescription());
            foundRecipe.setIngredients(recipe.getIngredients());
            foundRecipe.setDirections(recipe.getDirections());

            recipeService.updateRecipe(foundRecipe);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        try {
            recipeService.deleteRecipeById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
