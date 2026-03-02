package fr.vlegall.sochief.service.difficulty;

import fr.vlegall.sochief.contracts.common.NamedIdDto;
import fr.vlegall.sochief.model.recipe.RecipeDifficulty;
import org.springframework.stereotype.Component;

@Component
public class RecipeDifficultyMapper {
    public NamedIdDto toDto(RecipeDifficulty recipeDifficulty) {
        return new NamedIdDto(recipeDifficulty.getId(), recipeDifficulty.getName());
    }
}

