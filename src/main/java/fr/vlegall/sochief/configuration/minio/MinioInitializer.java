package fr.vlegall.sochief.configuration.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class MinioInitializer {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public MinioInitializer(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    @PostConstruct
    public void init() {
        String bucket = minioProperties.getBucket();
        if (bucket == null || bucket.isBlank()) {
            throw new IllegalArgumentException("MINIO_BUCKET is not configured");
        }
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot initialize MinIO bucket", e);
        }
    }
}

