package fr.vlegall.sochief.controller;

import fr.vlegall.sochief.contracts.response.ApiStatus;
import fr.vlegall.sochief.service.status.IStatusService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/status")
public class StatusController {
    private final IStatusService statusService;

    public StatusController(IStatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    @SecurityRequirement(name = "ApiKeyAuth")
    public ResponseEntity<ApiStatus> status() {
        return ResponseEntity.ok(statusService.getStatus());
    }
}

