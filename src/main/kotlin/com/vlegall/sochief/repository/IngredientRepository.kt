package com.vlegall.sochief.repository

import com.vlegall.sochief.model.recipe.Ingredient
import org.springframework.data.jpa.repository.JpaRepository

interface IngredientRepository : JpaRepository<Ingredient, Long> {
    fun findByNameIgnoreCase(name: String): Ingredient?
}
