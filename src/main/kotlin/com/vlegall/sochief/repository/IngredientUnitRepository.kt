package com.vlegall.sochief.repository

import com.vlegall.sochief.model.recipe.IngredientUnit
import org.springframework.data.jpa.repository.JpaRepository

interface IngredientUnitRepository : JpaRepository<IngredientUnit, Long> {
    fun findByNameIgnoreCase(name: String): IngredientUnit?
}
