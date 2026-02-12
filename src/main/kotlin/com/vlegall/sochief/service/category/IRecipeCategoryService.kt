package com.vlegall.sochief.service.category

import com.vlegall.sochiefcontracts.dto.common.NamedIdDto

interface IRecipeCategoryService {
    fun getRecipeCategories(): List<NamedIdDto>
}