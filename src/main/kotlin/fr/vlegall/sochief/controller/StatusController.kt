package fr.vlegall.sochief.controller

import fr.vlegall.sochief.contracts.response.ApiStatus
import fr.vlegall.sochief.service.status.IStatusService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/api/status")
class StatusController(
    private val statusService: IStatusService
) {


    @GetMapping
    @SecurityRequirement(name = "ApiKeyAuth")
    fun status(): ResponseEntity<ApiStatus> {
        return ResponseEntity.ok(statusService.getStatus())
    }
}
