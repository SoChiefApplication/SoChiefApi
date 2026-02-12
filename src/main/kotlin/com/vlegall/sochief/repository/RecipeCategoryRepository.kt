package com.vlegall.sochief.repository

import com.vlegall.sochief.model.recipe.RecipeCategory
import org.springframework.data.jpa.repository.JpaRepository

interface RecipeCategoryRepository : JpaRepository<RecipeCategory, Long> {
    fun findAllByOrderByIdAsc(): List<RecipeCategory>
}
