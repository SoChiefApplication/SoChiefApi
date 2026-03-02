package fr.vlegall.sochief;

import fr.vlegall.sochief.configuration.SecurityApiProperties;
import fr.vlegall.sochief.configuration.minio.MinioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({
        MinioProperties.class,
        SecurityApiProperties.class
})
@SpringBootApplication
public class SoChiefApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoChiefApplication.class, args);
    }
}

