package fr.vlegall.sochief.service.recipe;

import fr.vlegall.sochief.contracts.request.RecipeUpsertRequestDto;
import fr.vlegall.sochief.contracts.response.RecipeDetailDto;
import fr.vlegall.sochief.contracts.response.RecipeListItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface IRecipeService {
    Page<RecipeListItemDto> search(String q, Long categoryId, Pageable pageable);

    RecipeDetailDto getById(Long id, Integer portions);

    RecipeDetailDto create(RecipeUpsertRequestDto dto, MultipartFile image);

    RecipeDetailDto update(Long id, RecipeUpsertRequestDto dto, MultipartFile image);

    void delete(Long id);
}

