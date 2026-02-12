package com.vlegall.sochief.service.difficulty

import com.vlegall.sochief.repository.RecipeDifficultyRepository
import com.vlegall.sochiefcontracts.dto.common.NamedIdDto
import org.springframework.stereotype.Service

@Service
class RecipeDifficultyService(
    private val difficultyRepository: RecipeDifficultyRepository,
    private val recipeDifficultyMapper: RecipeDifficultyMapper
) : IRecipeDifficultyService {
    override fun getRecipeDifficulties(): List<NamedIdDto> {
        return difficultyRepository.findAllByOrderByIdAsc().map(recipeDifficultyMapper::toDto)
    }

}