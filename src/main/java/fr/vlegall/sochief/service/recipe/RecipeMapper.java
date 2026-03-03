package fr.vlegall.sochief.service.recipe;

import fr.vlegall.sochief.contracts.common.NamedIdDto;
import fr.vlegall.sochief.contracts.response.*;
import fr.vlegall.sochief.model.recipe.Recipe;
import fr.vlegall.sochief.model.recipe.RecipeCategory;
import fr.vlegall.sochief.model.recipe.RecipeDifficulty;
import fr.vlegall.sochief.service.category.RecipeCategoryMapper;
import fr.vlegall.sochief.service.difficulty.RecipeDifficultyMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeMapper {
    private final RecipeDifficultyMapper recipeDifficultyMapper;
    private final RecipeCategoryMapper recipeCategoryMapper;

    public RecipeMapper(RecipeDifficultyMapper recipeDifficultyMapper, RecipeCategoryMapper recipeCategoryMapper) {
        this.recipeDifficultyMapper = recipeDifficultyMapper;
        this.recipeCategoryMapper = recipeCategoryMapper;
    }

    public RecipeListItemDto toListItemDto(Recipe r) {
        return new RecipeListItemDto(
                r.getId(),
                r.getTitle() != null ? r.getTitle() : "",
                recipeCategoryMapper.toDto(r.getCategory() != null ? r.getCategory() : new RecipeCategory()),
                recipeDifficultyMapper.toDto(r.getDifficulty() != null ? r.getDifficulty() : new RecipeDifficulty()),
                r.getInitialPortions(),
                r.getPreparationTime().toString(),
                r.getCookingTime().toString(),
                null
        );
    }

    public RecipeDetailDto toDetailDto(Recipe r, Integer portions, ImageDto imageDto) {
        int basePortions = r.getInitialPortions();
        int displayed = portions != null ? portions : basePortions;
        BigDecimal ratio = new BigDecimal(displayed).divide(new BigDecimal(basePortions), 6, RoundingMode.HALF_UP);

        List<RecipeIngredientDto> ingredients = r.getIngredients().stream()
                .map(ri -> {
                    BigDecimal q = new BigDecimal(ri.getQuantity());
                    return new RecipeIngredientDto(
                            new NamedIdDto(ri.getIngredient().getId(), ri.getIngredient().getName() != null ? ri.getIngredient().getName() : ""),
                            new NamedIdDto(ri.getUnit().getId(), ri.getUnit().getName() != null ? ri.getUnit().getName() : ""),
                            displayed == basePortions ? q : q.multiply(ratio).stripTrailingZeros()
                    );
                })
                .collect(Collectors.toList());

        List<RecipeStepDto> steps = r.getSteps().stream()
                .sorted(Comparator.comparing(s -> s.getPosition() == null ? Integer.MAX_VALUE : s.getPosition()))
                .map(s -> new RecipeStepDto(
                        s.getId(),
                        s.getDescription(),
                        s.getDuration().toString(),
                        s.getPosition()
                ))
                .collect(Collectors.toList());

        List<NamedIdDto> tags = r.getTags().stream()
                .map(rt -> new NamedIdDto(rt.getTag().getId(), rt.getTag().getName() != null ? rt.getTag().getName() : ""))
                .collect(Collectors.toList());

        List<NamedIdDto> utensils = r.getUtensils().stream()
                .map(ru -> new NamedIdDto(ru.getUtensil().getId(), ru.getUtensil().getName() != null ? ru.getUtensil().getName() : ""))
                .collect(Collectors.toList());

        return new RecipeDetailDto(
                r.getId(),
                r.getTitle() != null ? r.getTitle() : "",
                r.getDescription(),
                recipeCategoryMapper.toDto(r.getCategory() != null ? r.getCategory() : new RecipeCategory()),
                recipeDifficultyMapper.toDto(r.getDifficulty() != null ? r.getDifficulty() : new RecipeDifficulty()),
                basePortions,
                displayed,
                r.getPreparationTime().toString(),
                r.getCookingTime().toString(),
                ingredients,
                steps,
                tags,
                utensils,
                imageDto
        );
    }
}

