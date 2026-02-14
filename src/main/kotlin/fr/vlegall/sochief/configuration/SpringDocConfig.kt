package fr.vlegall.sochief.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringDocConfig {
    @Bean
    fun customOpenAPI(): OpenAPI {
        val apiKeyScheme = SecurityScheme()
            .name("X-API-KEY")
            .type(SecurityScheme.Type.APIKEY)
            .`in`(SecurityScheme.In.HEADER)
        return OpenAPI()
            .components(Components().addSecuritySchemes("ApiKeyAuth", apiKeyScheme))
            .addSecurityItem(SecurityRequirement().addList("ApiKeyAuth"))
    }
}
