package fr.vlegall.sochief.controller;

import fr.vlegall.sochief.contracts.request.RecipeUpsertRequestDto;
import fr.vlegall.sochief.contracts.response.RecipeDetailDto;
import fr.vlegall.sochief.contracts.response.RecipeListItemDto;
import fr.vlegall.sochief.service.recipe.IRecipeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Validated
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final IRecipeService recipeService;

    public RecipeController(IRecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * Liste pageable + recherche full-text (q) + filtre category.
     * Ex:
     * GET /api/recipes?page=0&size=20&sort=title,asc
     * GET /api/recipes?q=poulet
     * GET /api/recipes?categoryId=3
     * GET /api/recipes?q=poulet&categoryId=3
     */
    @GetMapping
    @SecurityRequirement(name = "ApiKeyAuth")
    public ResponseEntity<Page<RecipeListItemDto>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<RecipeListItemDto> page = recipeService.search(q, categoryId, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Récupère une recette.
     * portions (optionnel) permet d'ajuster les quantités au moment de la lecture (sans persistance).
     * Ex: GET /api/recipes/12?portions=6
     */
    @GetMapping("/{id}")
    @SecurityRequirement(name = "ApiKeyAuth")
    public ResponseEntity<RecipeDetailDto> getById(
            @PathVariable Long id,
            @RequestParam(required = false) @Positive Integer portions
    ) {
        RecipeDetailDto dto = recipeService.getById(id, portions);
        return ResponseEntity.ok(dto);
    }

    /**
     * Création.
     * Retourne 201 + Location: /api/recipes/{id}
     */
    @PostMapping
    @SecurityRequirement(name = "ApiKeyAuth")
    public ResponseEntity<RecipeDetailDto> create(
            @Valid @RequestBody RecipeUpsertRequestDto body,
            UriComponentsBuilder uriBuilder
    ) {
        RecipeDetailDto created = recipeService.create(body);
        return ResponseEntity
                .created(uriBuilder.path("/api/recipes/{id}").buildAndExpand(created.getId()).toUri())
                .body(created);
    }

    /**
     * Mise à jour complète (PUT).
     */
    @PutMapping("/{id}")
    @SecurityRequirement(name = "ApiKeyAuth")
    public ResponseEntity<RecipeDetailDto> update(
            @PathVariable Long id,
            @Valid @RequestBody RecipeUpsertRequestDto body
    ) {
        RecipeDetailDto updated = recipeService.update(id, body);
        return ResponseEntity.ok(updated);
    }

    /**
     * Suppression.
     * 204 No Content
     */
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "ApiKeyAuth")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recipeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

