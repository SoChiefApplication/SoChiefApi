package fr.vlegall.sochief.service.recipe

import fr.vlegall.sochief.model.recipe.*
import fr.vlegall.sochief.contracts.request.RecipeUpsertRequestDto
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Duration

@Component
class RecipeAssembler(
    private val ref: RecipeRefService
) {

    fun apply(recipe: Recipe, dto: RecipeUpsertRequestDto) {
        recipe.title = dto.title.trim()
        recipe.description = dto.description
        recipe.category = ref.getCategory(dto.categoryId)
        recipe.difficulty = ref.getDifficulty(dto.difficultyId)
        recipe.initialPortions = dto.initialPortions
        recipe.preparationTime = Duration.parse(dto.preparationTime)
        recipe.cookingTime = Duration.parse(dto.cookingTime)

        // INGREDIENTS
        recipe.ingredients.clear()
        dto.ingredients.forEach { item ->
            val ingredient = ref.resolveIngredient(item.ingredient)
            val unit = ref.resolveUnit(item.unit)

            recipe.ingredients.add(
                RecipeIngredient(
                    recipe = recipe,
                    ingredient = ingredient,
                    unit = unit,
                    quantity = item.quantity.toLongStrict(), // voir helper ci-dessous
                )
            )
        }

        // STEPS
        recipe.steps.clear()
        val stepsSorted = dto.steps
            .mapIndexed { idx, s -> s.copy(position = s.position ?: (idx + 1)) }
            .sortedBy { it.position ?: Int.MAX_VALUE }

        stepsSorted.forEach { s ->
            recipe.steps.add(
                RecipeStep(
                    recipe = recipe,
                    description = s.description,
                    duration = Duration.parse(s.duration),
                    position = requireNotNull(s.position)
                )
            )
        }

        // TAGS (join entity)
        recipe.tags.clear()
        dto.tags.forEach { t ->
            val tag = ref.resolveTag(t)
            recipe.tags.add(RecipeTag(recipe = recipe, tag = tag))
        }

        // UTENSILS (join entity)
        recipe.utensils.clear()
        dto.utensils.forEach { u ->
            val utensil = ref.resolveUtensil(u)
            recipe.utensils.add(RecipeUtensil(recipe = recipe, utensil = utensil))
        }
    }

    private fun BigDecimal.toLongStrict(): Long {
        // Si tu veux accepter des décimales côté API, change l'entity `quantity` en BigDecimal.
        // Là on force entier.
        if (this.scale() > 0 && this.stripTrailingZeros().scale() > 0) {
            throw IllegalArgumentException("Ingredient quantity must be an integer (got $this)")
        }
        return this.longValueExact()
    }
}