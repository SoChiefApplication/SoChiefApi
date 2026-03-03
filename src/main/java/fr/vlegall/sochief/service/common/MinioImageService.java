package fr.vlegall.sochief.service.common;

import fr.vlegall.sochief.configuration.minio.MinioProperties;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioImageService {

    private final MinioClient minio;
    private final ImageValidatorService validator;
    private final MinioProperties minioProperties;

    public String upload(MultipartFile file) throws Exception {
        if (file.isEmpty()) throw new IllegalArgumentException("Fichier vide");

        InputStream raw = file.getInputStream();
        BufferedInputStream in = new BufferedInputStream(raw);
        String mime = validator.assertImageAndGetMime(in);

        String objectName = java.util.UUID.randomUUID() + guessExt(mime);

        minio.putObject(
                PutObjectArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .object(objectName)
                        .contentType(mime)
                        .stream(in, -1, 10 * 1024 * 1024)
                        .build()
        );

        return objectName;
    }

    public StatObjectResponse stat(String objectName) throws Exception {
        return minio.statObject(
                StatObjectArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .object(objectName)
                        .build()
        );
    }

    public InputStream getStream(String objectName) throws Exception {
        return minio.getObject(
                GetObjectArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .object(objectName)
                        .build()
        );
    }

    public String getImageUrl(String objectName) throws Exception {

        var stat = stat(objectName);

        if (stat.contentType() == null || !stat.contentType().startsWith("image/")) {
            throw new IllegalArgumentException("Objet non-image");
        }

        // On renvoie l'URL de TON API, pas celle de MinIO
        return "/api/images/" + objectName;
    }

    private String guessExt(String mime) {
        return switch (mime) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            case "image/bmp" -> ".bmp";
            case "image/tiff" -> ".tiff";
            default -> "";
        };
    }
}
