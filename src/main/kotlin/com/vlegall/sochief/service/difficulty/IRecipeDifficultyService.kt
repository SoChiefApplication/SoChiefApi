package com.vlegall.sochief.service.difficulty

import com.vlegall.sochiefcontracts.dto.common.NamedIdDto

interface IRecipeDifficultyService {
    fun getRecipeDifficulties(): List<NamedIdDto>
}