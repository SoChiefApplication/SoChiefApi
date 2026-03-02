package fr.vlegall.sochief.service.recipe;

import fr.vlegall.sochief.exception.NotFoundException;
import fr.vlegall.sochief.model.recipe.Recipe;
import fr.vlegall.sochief.repository.RecipeRepository;
import fr.vlegall.sochief.contracts.request.RecipeUpsertRequestDto;
import fr.vlegall.sochief.contracts.response.RecipeDetailDto;
import fr.vlegall.sochief.contracts.response.RecipeListItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecipeService implements IRecipeService {
    private final RecipeRepository recipeRepo;
    private final RecipeAssembler assembler;
    private final RecipeMapper recipeMapper;

    public RecipeService(RecipeRepository recipeRepo, RecipeAssembler assembler, RecipeMapper recipeMapper) {
        this.recipeRepo = recipeRepo;
        this.assembler = assembler;
        this.recipeMapper = recipeMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RecipeListItemDto> search(String q, Long categoryId, Pageable pageable) {
        return recipeRepo.search(q, categoryId, pageable).map(recipeMapper::toListItemDto);
    }

    @Transactional(readOnly = true)
    @Override
    public RecipeDetailDto getById(Long id, Integer portions) {
        Recipe recipe = recipeRepo.findDetailedById(id);
        if (recipe == null) {
            throw new NotFoundException("Recipe " + id + " not found");
        }
        if (portions != null && portions <= 0) {
            throw new IllegalArgumentException("portions must be > 0");
        }
        return recipeMapper.toDetailDto(recipe, portions);
    }

    @Transactional
    @Override
    public RecipeDetailDto create(RecipeUpsertRequestDto dto) {
        Recipe recipe = new Recipe();
        assembler.apply(recipe, dto);
        Recipe saved = recipeRepo.save(recipe);
        return recipeMapper.toDetailDto(saved, null);
    }

    @Transactional
    @Override
    public RecipeDetailDto update(Long id, RecipeUpsertRequestDto dto) {
        Recipe recipe = recipeRepo.findDetailedById(id);
        if (recipe == null) {
            throw new NotFoundException("Recipe " + id + " not found");
        }
        assembler.apply(recipe, dto);
        Recipe saved = recipeRepo.save(recipe);
        return recipeMapper.toDetailDto(saved, null);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!recipeRepo.existsById(id)) {
            throw new NotFoundException("Recipe " + id + " not found");
        }
        recipeRepo.deleteById(id);
    }
}

