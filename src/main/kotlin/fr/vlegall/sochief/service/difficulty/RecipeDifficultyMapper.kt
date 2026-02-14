package fr.vlegall.sochief.service.difficulty

import fr.vlegall.sochief.model.recipe.RecipeDifficulty
import fr.vlegall.sochief.contracts.common.NamedIdDto
import org.springframework.stereotype.Component

@Component
class RecipeDifficultyMapper {
    fun toDto(recipeDifficulty: RecipeDifficulty): NamedIdDto =
        NamedIdDto(requireNotNull(recipeDifficulty.id), requireNotNull(recipeDifficulty.name))
}