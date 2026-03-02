package fr.vlegall.sochief.repository;

import fr.vlegall.sochief.model.recipe.IngredientUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientUnitRepository extends JpaRepository<IngredientUnit, Long> {
    IngredientUnit findByNameIgnoreCase(String name);
}

