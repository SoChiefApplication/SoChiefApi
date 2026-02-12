package com.vlegall.sochief.service.recipe

import com.vlegall.sochief.exception.NotFoundException
import com.vlegall.sochief.model.recipe.Recipe
import com.vlegall.sochief.repository.RecipeRepository
import com.vlegall.sochiefcontracts.dto.request.RecipeUpsertRequestDto
import com.vlegall.sochiefcontracts.dto.response.RecipeDetailDto
import com.vlegall.sochiefcontracts.dto.response.RecipeListItemDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecipeService(
    private val recipeRepo: RecipeRepository,
    private val assembler: RecipeAssembler,
    private val recipeMapper: RecipeMapper
) : IRecipeService {

    @Transactional(readOnly = true)
    override fun search(q: String?, categoryId: Long?, pageable: Pageable): Page<RecipeListItemDto> =
        recipeRepo.search(q = q, categoryId = categoryId, pageable = pageable)
            .map { recipeMapper.toListItemDto(it) }

    @Transactional(readOnly = true)
    override fun getById(id: Long, portions: Int?): RecipeDetailDto {
        val recipe = recipeRepo.findDetailedById(id) ?: throw NotFoundException("Recipe $id not found")
        if (portions != null && portions <= 0) {
            throw IllegalArgumentException("portions must be > 0")
        }
        return recipeMapper.toDetailDto(recipe, portions)
    }

    @Transactional
    override fun create(dto: RecipeUpsertRequestDto): RecipeDetailDto {
        val recipe = Recipe()
        assembler.apply(recipe, dto)
        val saved = recipeRepo.save(recipe)
        return recipeMapper.toDetailDto(saved, portions = null)
    }

    @Transactional
    override fun update(id: Long, dto: RecipeUpsertRequestDto): RecipeDetailDto {
        val recipe = recipeRepo.findDetailedById(id) ?: throw NotFoundException("Recipe $id not found")
        assembler.apply(recipe, dto)
        val saved = recipeRepo.save(recipe)
        return recipeMapper.toDetailDto(saved, portions = null)
    }

    @Transactional
    override fun delete(id: Long) {
        if (!recipeRepo.existsById(id)) throw NotFoundException("Recipe $id not found")
        recipeRepo.deleteById(id)
    }
}
