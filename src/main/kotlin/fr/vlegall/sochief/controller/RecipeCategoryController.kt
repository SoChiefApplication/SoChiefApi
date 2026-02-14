package fr.vlegall.sochief.controller

import fr.vlegall.sochief.service.category.RecipeCategoryService
import fr.vlegall.sochief.contracts.common.NamedIdDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api/recipes/category")
class RecipeCategoryController(
    private val recipeCategoryService: RecipeCategoryService
) {

    /**
     * Liste.
     *
     * Ex:
     *  GET /api/recipes/difficulty
     */
    @GetMapping
    @SecurityRequirement(name = "ApiKeyAuth")
    fun search(
    ): ResponseEntity<List<NamedIdDto>> {
        return ResponseEntity.ok(recipeCategoryService.getRecipeCategories())
    }
}
