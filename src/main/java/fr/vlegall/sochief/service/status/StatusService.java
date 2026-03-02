package fr.vlegall.sochief.service.status;

import fr.vlegall.sochief.contracts.response.ApiStatus;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class StatusService implements IStatusService {
    private final Environment environment;
    private final BuildProperties build;

    public StatusService(Environment environment, BuildProperties build) {
        this.environment = environment;
        this.build = build;
    }

    @Override
    public ApiStatus getStatus() {
        String[] profiles = environment.getActiveProfiles();
        String profile = profiles.length > 0 ? profiles[0] : "Default";
        String version = build != null ? build.getVersion() : "dev";
        String name = build != null ? build.getName() : (environment.getProperty("spring.application.name") != null
                ? environment.getProperty("spring.application.name") : "unknown");
        return new ApiStatus(name, profile, version, true);
    }
}

