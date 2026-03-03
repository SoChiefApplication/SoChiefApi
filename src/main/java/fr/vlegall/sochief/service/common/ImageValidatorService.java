package fr.vlegall.sochief.service.common;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ImageValidatorService {
    private final Tika tika = new Tika();

    public String assertImageAndGetMime(InputStream in) throws IOException {
        if (!in.markSupported()) {
            in = new BufferedInputStream(in);
        }
        in.mark(64 * 1024);

        String mime = tika.detect(in);
        in.reset();

        if (mime == null || !mime.startsWith("image/")) {
            throw new IllegalArgumentException("Seules les images sont acceptées (fichier détecté: " + mime + ")");
        }
        return mime;
    }
}
