package fr.vlegall.sochief.repository

import fr.vlegall.sochief.model.recipe.Ingredient
import org.springframework.data.jpa.repository.JpaRepository

interface IngredientRepository : JpaRepository<Ingredient, Long> {
    fun findByNameIgnoreCase(name: String): Ingredient?
}
