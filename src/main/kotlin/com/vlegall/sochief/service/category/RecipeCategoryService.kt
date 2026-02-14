package com.vlegall.sochief.service.category

import com.vlegall.sochief.repository.RecipeCategoryRepository
import fr.vlegall.sochief.contracts.common.NamedIdDto
import org.springframework.stereotype.Service

@Service
class RecipeCategoryService(
    private val recipeCategoryMapper: RecipeCategoryMapper,
    private val recipeCategoryRepository: RecipeCategoryRepository
) : IRecipeCategoryService {
    override fun getRecipeCategories(): List<NamedIdDto> {
        return recipeCategoryRepository.findAllByOrderByIdAsc().map(recipeCategoryMapper::toDto)
    }

}