package fr.vlegall.sochief.service.recipe

import fr.vlegall.sochief.contracts.request.RecipeUpsertRequestDto
import fr.vlegall.sochief.contracts.response.RecipeDetailDto
import fr.vlegall.sochief.contracts.response.RecipeListItemDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IRecipeService {
    fun search(q: String?, categoryId: Long?, pageable: Pageable): Page<RecipeListItemDto>
    fun getById(id: Long, portions: Int? = null): RecipeDetailDto
    fun create(dto: RecipeUpsertRequestDto): RecipeDetailDto
    fun update(id: Long, dto: RecipeUpsertRequestDto): RecipeDetailDto
    fun delete(id: Long)
}