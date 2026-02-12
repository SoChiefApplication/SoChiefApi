package com.vlegall.sochief.controller

import com.vlegall.sochief.service.recipe.IRecipeService
import com.vlegall.sochiefcontracts.dto.request.RecipeUpsertRequestDto
import com.vlegall.sochiefcontracts.dto.response.RecipeDetailDto
import com.vlegall.sochiefcontracts.dto.response.RecipeListItemDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@Validated
@RestController
@RequestMapping("/api/recipes")
class RecipeController(
    private val recipeService: IRecipeService
) {

    /**
     * Liste pageable + recherche full-text (q) + filtre category.
     *
     * Ex:
     *  GET /api/recipes?page=0&size=20&sort=title,asc
     *  GET /api/recipes?q=poulet
     *  GET /api/recipes?categoryId=3
     *  GET /api/recipes?q=poulet&categoryId=3
     */
    @GetMapping
    @SecurityRequirement(name = "ApiKeyAuth")
    fun search(
        @RequestParam(required = false) q: String?,
        @RequestParam(required = false) categoryId: Long?,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<RecipeListItemDto>> {
        val page = recipeService.search(q = q, categoryId = categoryId, pageable = pageable)
        return ResponseEntity.ok(page)
    }

    /**
     * Récupère une recette.
     * portions (optionnel) permet d'ajuster les quantités au moment de la lecture (sans persistance).
     *
     * Ex: GET /api/recipes/12?portions=6
     */
    @GetMapping("/{id}")
    @SecurityRequirement(name = "ApiKeyAuth")
    fun getById(
        @PathVariable id: Long,
        @RequestParam(required = false) @Positive portions: Int?
    ): ResponseEntity<RecipeDetailDto> {
        val dto = recipeService.getById(id = id, portions = portions)
        return ResponseEntity.ok(dto)
    }

    /**
     * Création.
     * Retourne 201 + Location: /api/recipes/{id}
     */
    @PostMapping
    @SecurityRequirement(name = "ApiKeyAuth")
    fun create(
        @Valid @RequestBody body: RecipeUpsertRequestDto,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<RecipeDetailDto> {
        val created = recipeService.create(body)
        val location = uriBuilder
            .path("/api/recipes/{id}")
            .buildAndExpand(created.id)
            .toUri()

        return ResponseEntity.created(location).body(created)
    }

    /**
     * Mise à jour complète (PUT).
     */
    @PutMapping("/{id}")
    @SecurityRequirement(name = "ApiKeyAuth")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody body: RecipeUpsertRequestDto
    ): ResponseEntity<RecipeDetailDto> {
        val updated = recipeService.update(id, body)
        return ResponseEntity.ok(updated)
    }

    /**
     * Suppression.
     * 204 No Content
     */
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "ApiKeyAuth")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        recipeService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
