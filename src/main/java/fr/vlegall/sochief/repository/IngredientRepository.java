package fr.vlegall.sochief.repository;

import fr.vlegall.sochief.model.recipe.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Ingredient findByNameIgnoreCase(String name);
}

