package fr.vlegall.sochief.repository;

import fr.vlegall.sochief.model.recipe.RecipeDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeDifficultyRepository extends JpaRepository<RecipeDifficulty, Long> {
    List<RecipeDifficulty> findAllByOrderByIdAsc();
}

