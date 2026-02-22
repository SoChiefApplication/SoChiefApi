package fr.vlegall.sochief.service.status

import fr.vlegall.sochief.contracts.response.ApiStatus
import org.springframework.core.env.Environment
import org.springframework.boot.info.BuildProperties
import org.springframework.stereotype.Service

@Service
class StatusService(
    private val environment: Environment,
    private val build: BuildProperties?
): IStatusService {


    override fun getStatus(): ApiStatus {
        val profile = environment.activeProfiles.firstOrNull() ?: "Default"
        val version = build?.version ?: "dev"
        val name = build?.name ?: environment.getProperty("spring.application.name") ?: "unknown"

        return ApiStatus(name, profile, version, true)
    }
}