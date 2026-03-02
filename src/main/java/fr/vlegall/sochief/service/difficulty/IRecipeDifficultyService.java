package fr.vlegall.sochief.service.difficulty;

import fr.vlegall.sochief.contracts.common.NamedIdDto;

import java.util.List;

public interface IRecipeDifficultyService {
    List<NamedIdDto> getRecipeDifficulties();
}

