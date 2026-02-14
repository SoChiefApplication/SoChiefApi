package fr.vlegall.sochief.service.difficulty

import fr.vlegall.sochief.contracts.common.NamedIdDto

interface IRecipeDifficultyService {
    fun getRecipeDifficulties(): List<NamedIdDto>
}