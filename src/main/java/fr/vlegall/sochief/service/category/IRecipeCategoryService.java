package fr.vlegall.sochief.service.category;

import fr.vlegall.sochief.contracts.common.NamedIdDto;

import java.util.List;

public interface IRecipeCategoryService {
    List<NamedIdDto> getRecipeCategories();
}

