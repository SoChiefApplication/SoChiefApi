package fr.vlegall.sochief.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "security.api")
public class SecurityApiProperties {
    private List<String> keys = new ArrayList<>();
}

