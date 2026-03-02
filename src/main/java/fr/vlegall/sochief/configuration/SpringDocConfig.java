package fr.vlegall.sochief.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme apiKeyScheme = new SecurityScheme()
                .name("X-API-KEY")
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER);
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("ApiKeyAuth", apiKeyScheme))
                .addSecurityItem(new SecurityRequirement().addList("ApiKeyAuth"));
    }
}

