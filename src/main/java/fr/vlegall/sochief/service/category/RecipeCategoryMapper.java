package fr.vlegall.sochief.service.category;

import fr.vlegall.sochief.contracts.common.NamedIdDto;
import fr.vlegall.sochief.model.recipe.RecipeCategory;
import org.springframework.stereotype.Component;

@Component
public class RecipeCategoryMapper {
    public NamedIdDto toDto(RecipeCategory recipeCategory) {
        return new NamedIdDto(recipeCategory.getId(), recipeCategory.getName());
        }
}

