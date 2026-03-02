package fr.vlegall.sochief.service.recipe;

import fr.vlegall.sochief.model.recipe.*;
import fr.vlegall.sochief.contracts.request.RecipeUpsertRequestDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@Component
public class RecipeAssembler {
    private final RecipeRefService ref;

    public RecipeAssembler(RecipeRefService ref) {
        this.ref = ref;
    }

    public void apply(Recipe recipe, RecipeUpsertRequestDto dto) {
        recipe.setTitle(dto.getTitle().trim());
        recipe.setDescription(dto.getDescription());
        recipe.setCategory(ref.getCategory(dto.getCategoryId()));
        recipe.setDifficulty(ref.getDifficulty(dto.getDifficultyId()));
        recipe.setInitialPortions(dto.getInitialPortions());
        recipe.setPreparationTime(Duration.parse(dto.getPreparationTime()));
        recipe.setCookingTime(Duration.parse(dto.getCookingTime()));

        // INGREDIENTS
        recipe.getIngredients().clear();
        dto.getIngredients().forEach(item -> {
            Ingredient ingredient = ref.resolveIngredient(item.getIngredient());
            IngredientUnit unit = ref.resolveUnit(item.getUnit());
            RecipeIngredient ri = new RecipeIngredient();
            ri.setRecipe(recipe);
            ri.setIngredient(ingredient);
            ri.setUnit(unit);
            ri.setQuantity(toLongStrict(item.getQuantity()));
            recipe.getIngredients().add(ri);
        });

        // STEPS
        recipe.getSteps().clear();
        // Compute positions and sort by position ascending
        List<fr.vlegall.sochief.contracts.request.RecipeStepUpsertDto> stepsSorted = dto.getSteps().stream()
                .sorted(Comparator.comparing(s -> s.getPosition() == null ? Integer.MAX_VALUE : s.getPosition()))
                .toList();
        int idx = 1;
        for (fr.vlegall.sochief.contracts.request.RecipeStepUpsertDto s : stepsSorted) {
            RecipeStep step = new RecipeStep();
            step.setRecipe(recipe);
            step.setDescription(s.getDescription());
            step.setDuration(Duration.parse(s.getDuration()));
            int position = s.getPosition() != null && s.getPosition() > 0 ? s.getPosition() : idx;
            step.setPosition(position);
            idx++;
            recipe.getSteps().add(step);
        }

        // TAGS
        recipe.getTags().clear();
        dto.getTags().forEach(t -> {
            Tag tag = ref.resolveTag(t);
            RecipeTag rt = new RecipeTag();
            rt.setRecipe(recipe);
            rt.setTag(tag);
            recipe.getTags().add(rt);
        });

        // UTENSILS
        recipe.getUtensils().clear();
        dto.getUtensils().forEach(u -> {
            Utensil utensil = ref.resolveUtensil(u);
            RecipeUtensil ru = new RecipeUtensil();
            ru.setRecipe(recipe);
            ru.setUtensil(utensil);
            recipe.getUtensils().add(ru);
        });
    }

    private long toLongStrict(BigDecimal value) {
        if (value.scale() > 0 && value.stripTrailingZeros().scale() > 0) {
            throw new IllegalArgumentException("Ingredient quantity must be an integer (got " + value + ")");
        }
        return value.longValueExact();
    }
}
