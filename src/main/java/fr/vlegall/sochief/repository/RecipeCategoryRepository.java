package fr.vlegall.sochief.repository;

import fr.vlegall.sochief.model.recipe.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {
    List<RecipeCategory> findAllByOrderByIdAsc();
}

