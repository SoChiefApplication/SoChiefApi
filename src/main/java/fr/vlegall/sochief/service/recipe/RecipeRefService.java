package fr.vlegall.sochief.service.recipe;

import fr.vlegall.sochief.contracts.common.IdOrNameDto;
import fr.vlegall.sochief.exception.NotFoundException;
import fr.vlegall.sochief.model.recipe.*;
import fr.vlegall.sochief.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecipeRefService {
    private final RecipeCategoryRepository categoryRepo;
    private final RecipeDifficultyRepository difficultyRepo;
    private final IngredientRepository ingredientRepo;
    private final IngredientUnitRepository unitRepo;
    private final TagRepository tagRepo;
    private final UtensilRepository utensilRepo;

    public RecipeRefService(RecipeCategoryRepository categoryRepo,
                            RecipeDifficultyRepository difficultyRepo,
                            IngredientRepository ingredientRepo,
                            IngredientUnitRepository unitRepo,
                            TagRepository tagRepo,
                            UtensilRepository utensilRepo) {
        this.categoryRepo = categoryRepo;
        this.difficultyRepo = difficultyRepo;
        this.ingredientRepo = ingredientRepo;
        this.unitRepo = unitRepo;
        this.tagRepo = tagRepo;
        this.utensilRepo = utensilRepo;
    }

    @Transactional(readOnly = true)
    public RecipeCategory getCategory(Long categoryId) {
        return categoryRepo.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("RecipeCategory " + categoryId + " not found"));
    }

    @Transactional(readOnly = true)
    public RecipeDifficulty getDifficulty(Long difficultyId) {
        return difficultyRepo.findById(difficultyId)
                .orElseThrow(() -> new NotFoundException("RecipeDifficulty " + difficultyId + " not found"));
    }

    @Transactional
    public Ingredient resolveIngredient(IdOrNameDto ref) {
        return resolveEntity(
                ref,
                id -> ingredientRepo.findById(id).orElse(null),
                ingredientRepo::findByNameIgnoreCase,
                name -> ingredientRepo.save(new Ingredient(null, name))
                , "Ingredient");
    }

    @Transactional
    public IngredientUnit resolveUnit(IdOrNameDto ref) {
        return resolveEntity(
                ref,
                id -> unitRepo.findById(id).orElse(null),
                unitRepo::findByNameIgnoreCase,
                name -> unitRepo.save(new IngredientUnit(null, name))
                , "IngredientUnit");
    }

    @Transactional
    public Tag resolveTag(IdOrNameDto ref) {
        return resolveEntity(
                ref,
                id -> tagRepo.findById(id).orElse(null),
                tagRepo::findByNameIgnoreCase,
                name -> tagRepo.save(new Tag(null, name))
                , "Tag");
    }

    @Transactional
    public Utensil resolveUtensil(IdOrNameDto ref) {
        return resolveEntity(
                ref,
                id -> utensilRepo.findById(id).orElse(null),
                utensilRepo::findByNameIgnoreCase,
                name -> utensilRepo.save(new Utensil(null, name))
                , "Utensil");
    }

    private <T> T resolveEntity(IdOrNameDto ref,
                                java.util.function.Function<Long, T> byId,
                                java.util.function.Function<String, T> byName,
                                java.util.function.Function<String, T> create,
                                String label) {
        if (ref.getId() != null) {
            T found = byId.apply(ref.getId());
            if (found == null) throw new NotFoundException(label + " " + ref.getId() + " not found");
            return found;
        }
        String name = ref.getName() != null ? ref.getName().trim() : "";
        if (name.isBlank()) {
            throw new IllegalArgumentException(label + " reference must contain id or name");
        }
        T found = byName.apply(name);
        return found != null ? found : create.apply(name);
    }
}

