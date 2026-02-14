package fr.vlegall.sochief.service.category

import fr.vlegall.sochief.contracts.common.NamedIdDto


interface IRecipeCategoryService {
    fun getRecipeCategories(): List<NamedIdDto>
}