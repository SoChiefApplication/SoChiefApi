package fr.vlegall.sochief.service.difficulty;

import fr.vlegall.sochief.contracts.common.NamedIdDto;
import fr.vlegall.sochief.repository.RecipeDifficultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeDifficultyService implements IRecipeDifficultyService {
    private final RecipeDifficultyRepository difficultyRepository;
    private final RecipeDifficultyMapper recipeDifficultyMapper;

    public RecipeDifficultyService(RecipeDifficultyRepository difficultyRepository, RecipeDifficultyMapper recipeDifficultyMapper) {
        this.difficultyRepository = difficultyRepository;
        this.recipeDifficultyMapper = recipeDifficultyMapper;
    }

    @Override
    public List<NamedIdDto> getRecipeDifficulties() {
        return difficultyRepository.findAllByOrderByIdAsc()
                .stream()
                .map(recipeDifficultyMapper::toDto)
                .collect(Collectors.toList());
    }
}

