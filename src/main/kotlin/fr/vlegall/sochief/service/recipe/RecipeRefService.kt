package fr.vlegall.sochief.service.recipe

import fr.vlegall.sochief.exception.NotFoundException
import fr.vlegall.sochief.model.recipe.*
import fr.vlegall.sochief.repository.*
import fr.vlegall.sochief.contracts.common.IdOrNameDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecipeRefService(
    private val categoryRepo: RecipeCategoryRepository,
    private val difficultyRepo: RecipeDifficultyRepository,
    private val ingredientRepo: IngredientRepository,
    private val unitRepo: IngredientUnitRepository,
    private val tagRepo: TagRepository,
    private val utensilRepo: UtensilRepository,
) {

    @Transactional(readOnly = true)
    fun getCategory(categoryId: Long): RecipeCategory =
        categoryRepo.findById(categoryId).orElseThrow {
            NotFoundException("RecipeCategory $categoryId not found")
        }

    @Transactional(readOnly = true)
    fun getDifficulty(difficultyId: Long): RecipeDifficulty =
        difficultyRepo.findById(difficultyId).orElseThrow {
            NotFoundException("RecipeDifficulty $difficultyId not found")
        }

    @Transactional
    fun resolveIngredient(ref: IdOrNameDto): Ingredient =
        resolveEntity(
            ref = ref,
            byId = { ingredientRepo.findById(it).orElse(null) },
            byName = { ingredientRepo.findByNameIgnoreCase(it) },
            create = { ingredientRepo.save(Ingredient(name = it)) },
            label = "Ingredient"
        )

    @Transactional
    fun resolveUnit(ref: IdOrNameDto): IngredientUnit =
        resolveEntity(
            ref = ref,
            byId = { unitRepo.findById(it).orElse(null) },
            byName = { unitRepo.findByNameIgnoreCase(it) },
            create = { unitRepo.save(IngredientUnit(name = it)) },
            label = "IngredientUnit"
        )

    @Transactional
    fun resolveTag(ref: IdOrNameDto): Tag =
        resolveEntity(
            ref = ref,
            byId = { tagRepo.findById(it).orElse(null) },
            byName = { tagRepo.findByNameIgnoreCase(it) },
            create = { tagRepo.save(Tag(name = it)) },
            label = "Tag"
        )

    @Transactional
    fun resolveUtensil(ref: IdOrNameDto): Utensil =
        resolveEntity(
            ref = ref,
            byId = { utensilRepo.findById(it).orElse(null) },
            byName = { utensilRepo.findByNameIgnoreCase(it) },
            create = { utensilRepo.save(Utensil(name = it)) },
            label = "Utensil"
        )

    private fun <T> resolveEntity(
        ref: IdOrNameDto,
        byId: (Long) -> T?,
        byName: (String) -> T?,
        create: (String) -> T,
        label: String
    ): T {
        ref.id?.let { id ->
            return byId(id) ?: throw NotFoundException("$label $id not found")
        }
        val name = ref.name?.trim().orEmpty()
        if (name.isBlank()) throw IllegalArgumentException("$label reference must contain id or name")
        return byName(name) ?: create(name)
    }
}