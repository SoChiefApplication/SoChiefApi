package com.vlegall.sochief.service.difficulty

import com.vlegall.sochief.model.recipe.RecipeDifficulty
import com.vlegall.sochiefcontracts.dto.common.NamedIdDto
import org.springframework.stereotype.Component

@Component
class RecipeDifficultyMapper {
    fun toDto(recipeDifficulty: RecipeDifficulty): NamedIdDto =
        NamedIdDto(requireNotNull(recipeDifficulty.id), requireNotNull(recipeDifficulty.name))
}