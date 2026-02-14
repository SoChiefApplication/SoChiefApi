package com.vlegall.sochief.controller

import com.vlegall.sochief.service.difficulty.IRecipeDifficultyService
import fr.vlegall.sochief.contracts.common.NamedIdDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api/recipes/difficulty")
class RecipeDifficultyController(
    private val recipeDifficultyService: IRecipeDifficultyService
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
        return ResponseEntity.ok(recipeDifficultyService.getRecipeDifficulties())
    }
}
