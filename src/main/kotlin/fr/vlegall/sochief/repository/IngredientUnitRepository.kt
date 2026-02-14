package fr.vlegall.sochief.repository

import fr.vlegall.sochief.model.recipe.IngredientUnit
import org.springframework.data.jpa.repository.JpaRepository

interface IngredientUnitRepository : JpaRepository<IngredientUnit, Long> {
    fun findByNameIgnoreCase(name: String): IngredientUnit?
}
