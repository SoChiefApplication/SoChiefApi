package fr.vlegall.sochief.repository

import fr.vlegall.sochief.model.recipe.RecipeDifficulty
import org.springframework.data.jpa.repository.JpaRepository

interface RecipeDifficultyRepository : JpaRepository<RecipeDifficulty, Long> {
    fun findAllByOrderByIdAsc(): List<RecipeDifficulty>
}
