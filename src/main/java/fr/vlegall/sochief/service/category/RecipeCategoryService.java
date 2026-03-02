package fr.vlegall.sochief.service.category;

import fr.vlegall.sochief.contracts.common.NamedIdDto;
import fr.vlegall.sochief.repository.RecipeCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeCategoryService implements IRecipeCategoryService {
    private final RecipeCategoryMapper recipeCategoryMapper;
    private final RecipeCategoryRepository recipeCategoryRepository;

    public RecipeCategoryService(RecipeCategoryMapper recipeCategoryMapper, RecipeCategoryRepository recipeCategoryRepository) {
        this.recipeCategoryMapper = recipeCategoryMapper;
        this.recipeCategoryRepository = recipeCategoryRepository;
    }

    @Override
    public List<NamedIdDto> getRecipeCategories() {
        return recipeCategoryRepository.findAllByOrderByIdAsc()
                .stream()
                .map(recipeCategoryMapper::toDto)
                .collect(Collectors.toList());
    }
}

