package fr.vlegall.sochief.service.recipe;

import fr.vlegall.sochief.contracts.request.RecipeUpsertRequestDto;
import fr.vlegall.sochief.contracts.response.ImageDto;
import fr.vlegall.sochief.contracts.response.RecipeDetailDto;
import fr.vlegall.sochief.contracts.response.RecipeListItemDto;
import fr.vlegall.sochief.exception.NotFoundException;
import fr.vlegall.sochief.model.recipe.Recipe;
import fr.vlegall.sochief.repository.RecipeRepository;
import fr.vlegall.sochief.service.common.MinioImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RecipeService implements IRecipeService {
    private final RecipeRepository recipeRepo;
    private final RecipeAssembler assembler;
    private final RecipeMapper recipeMapper;
    private final MinioImageService minioImageService;

    @Transactional(readOnly = true)
    @Override
    public Page<RecipeListItemDto> search(String q, Long categoryId, Pageable pageable) {
        return recipeRepo.search(q, categoryId, pageable).map(recipeMapper::toListItemDto);
    }

    @Transactional(readOnly = true)
    @Override
    public RecipeDetailDto getById(Long id, Integer portions) {
        try {
            Recipe recipe = recipeRepo.findDetailedById(id);
            if (recipe == null) {
                throw new NotFoundException("Recipe " + id + " not found");
            }
            if (portions != null && portions <= 0) {
                throw new IllegalArgumentException("portions must be > 0");
            }
            ImageDto imageDto = null;
            if (recipe.getMinioObjectKeyImg() != null) {
                String presigned = minioImageService.getImageUrl(recipe.getMinioObjectKeyImg());
                imageDto = new ImageDto(
                        presigned,
                        presigned
                );
            }
            return recipeMapper.toDetailDto(recipe, portions, imageDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    @Override
    public RecipeDetailDto create(RecipeUpsertRequestDto dto, MultipartFile image) {
        try {
            Recipe recipe = new Recipe();
            assembler.apply(recipe, dto);
            if (image != null) {
                recipe.setMinioObjectKeyImg(minioImageService.upload(image));
            }
            return getRecipeDetailDto(dto, recipe);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public RecipeDetailDto update(Long id, RecipeUpsertRequestDto dto, MultipartFile image) {
        try {
            Recipe recipe = recipeRepo.findDetailedById(id);
            if (recipe == null) {
                throw new NotFoundException("Recipe " + id + " not found");
            }
            assembler.apply(recipe, dto);
            return getRecipeDetailDto(dto, recipe);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private RecipeDetailDto getRecipeDetailDto(RecipeUpsertRequestDto dto, Recipe recipe) throws Exception {
        Recipe saved = recipeRepo.save(recipe);
        ImageDto imageDto = null;
        if (saved.getMinioObjectKeyImg() != null && !dto.getRemoveImage()) {
            String presigned = minioImageService.getImageUrl(recipe.getMinioObjectKeyImg());
            imageDto = new ImageDto(
                    presigned,
                    presigned
            );
        }
        return recipeMapper.toDetailDto(saved, null, imageDto);
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

