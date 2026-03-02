package fr.vlegall.sochief.controller;

import fr.vlegall.sochief.contracts.common.NamedIdDto;
import fr.vlegall.sochief.service.difficulty.IRecipeDifficultyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/recipes/difficulty")
public class RecipeDifficultyController {
    private final IRecipeDifficultyService recipeDifficultyService;

    public RecipeDifficultyController(IRecipeDifficultyService recipeDifficultyService) {
        this.recipeDifficultyService = recipeDifficultyService;
    }

    /**
     * Liste.
     * Ex:
     * GET /api/recipes/difficulty
     */
    @GetMapping
    @SecurityRequirement(name = "ApiKeyAuth")
    public ResponseEntity<List<NamedIdDto>> search() {
        return ResponseEntity.ok(recipeDifficultyService.getRecipeDifficulties());
    }
}

