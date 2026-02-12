package com.vlegall.sochief.service.recipe

import com.vlegall.sochiefcontracts.dto.request.RecipeUpsertRequestDto
import com.vlegall.sochiefcontracts.dto.response.RecipeDetailDto
import com.vlegall.sochiefcontracts.dto.response.RecipeListItemDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IRecipeService {
    fun search(q: String?, categoryId: Long?, pageable: Pageable): Page<RecipeListItemDto>
    fun getById(id: Long, portions: Int? = null): RecipeDetailDto
    fun create(dto: RecipeUpsertRequestDto): RecipeDetailDto
    fun update(id: Long, dto: RecipeUpsertRequestDto): RecipeDetailDto
    fun delete(id: Long)
}