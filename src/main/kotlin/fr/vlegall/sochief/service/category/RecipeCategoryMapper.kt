package fr.vlegall.sochief.service.category

import fr.vlegall.sochief.model.recipe.RecipeCategory
import fr.vlegall.sochief.contracts.common.NamedIdDto
import org.springframework.stereotype.Component

@Component
class RecipeCategoryMapper {
    fun toDto(recipeCategory: RecipeCategory): NamedIdDto =
        NamedIdDto(requireNotNull(recipeCategory.id), requireNotNull(recipeCategory.name))
}