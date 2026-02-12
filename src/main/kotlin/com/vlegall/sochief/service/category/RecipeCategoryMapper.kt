package com.vlegall.sochief.service.category

import com.vlegall.sochief.model.recipe.RecipeCategory
import com.vlegall.sochiefcontracts.dto.common.NamedIdDto
import org.springframework.stereotype.Component

@Component
class RecipeCategoryMapper {
    fun toDto(recipeCategory: RecipeCategory): NamedIdDto =
        NamedIdDto(requireNotNull(recipeCategory.id), requireNotNull(recipeCategory.name))
}