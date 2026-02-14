package fr.vlegall.sochief.service.recipe

import fr.vlegall.sochief.model.recipe.Recipe
import fr.vlegall.sochief.model.recipe.RecipeCategory
import fr.vlegall.sochief.model.recipe.RecipeDifficulty
import fr.vlegall.sochief.service.category.RecipeCategoryMapper
import fr.vlegall.sochief.service.difficulty.RecipeDifficultyMapper
import fr.vlegall.sochief.contracts.common.NamedIdDto
import fr.vlegall.sochief.contracts.response.RecipeDetailDto
import fr.vlegall.sochief.contracts.response.RecipeIngredientDto
import fr.vlegall.sochief.contracts.response.RecipeListItemDto
import fr.vlegall.sochief.contracts.response.RecipeStepDto
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode

@Component
class RecipeMapper(
    private val recipeDifficultyMapper: RecipeDifficultyMapper,
    private val recipeCategoryMapper: RecipeCategoryMapper
) {

    fun toListItemDto(r: Recipe): RecipeListItemDto =
        RecipeListItemDto(
            id = requireNotNull(r.id),
            title = r.title.orEmpty(),
            category = recipeCategoryMapper.toDto(r.category ?: RecipeCategory()),
            difficulty = recipeDifficultyMapper.toDto(r.difficulty ?: RecipeDifficulty()),
            initialPortions = requireNotNull(r.initialPortions),
            preparationTime = requireNotNull(r.preparationTime).toString(),
            cookingTime = requireNotNull(r.cookingTime).toString()
        )

    fun toDetailDto(r: Recipe, portions: Int?): RecipeDetailDto {
        val basePortions = requireNotNull(r.initialPortions)
        val displayed = portions ?: basePortions
        val ratio = BigDecimal(displayed).divide(BigDecimal(basePortions), 6, RoundingMode.HALF_UP)

        return RecipeDetailDto(
            id = requireNotNull(r.id),
            title = r.title.orEmpty(),
            description = r.description,
            category = recipeCategoryMapper.toDto(r.category ?: RecipeCategory()),
            difficulty = recipeDifficultyMapper.toDto(r.difficulty ?: RecipeDifficulty()),
            initialPortions = basePortions,
            displayedPortions = displayed,
            preparationTime = requireNotNull(r.preparationTime).toString(),
            cookingTime = requireNotNull(r.cookingTime).toString(),
            ingredients = r.ingredients.map { ri ->
                val q = BigDecimal(requireNotNull(ri.quantity))
                RecipeIngredientDto(
                    ingredient = NamedIdDto(requireNotNull(ri.ingredient?.id), ri.ingredient?.name.orEmpty()),
                    unit = NamedIdDto(requireNotNull(ri.unit?.id), ri.unit?.name.orEmpty()),
                    quantity = if (displayed == basePortions) q else q.multiply(ratio).stripTrailingZeros()
                )
            },
            steps = r.steps
                .sortedBy { it.position ?: Int.MAX_VALUE }
                .map { s ->
                    RecipeStepDto(
                        id = s.id,
                        description = s.description,
                        duration = requireNotNull(s.duration).toString(),
                        position = requireNotNull(s.position)
                    )
                },
            tags = r.tags.map { rt ->
                NamedIdDto(requireNotNull(rt.tag?.id), rt.tag?.name.orEmpty())
            },
            utensils = r.utensils.map { ru ->
                NamedIdDto(requireNotNull(ru.utensil?.id), ru.utensil?.name.orEmpty())
            }
        )
    }
}