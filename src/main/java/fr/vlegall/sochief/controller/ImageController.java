package fr.vlegall.sochief.controller;

import fr.vlegall.sochief.service.common.MinioImageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.io.OutputStream;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final MinioImageService minioImageService;

    @GetMapping("/{objectName}")
    @SecurityRequirement(name = "ApiKeyAuth")
    public void getImage(@PathVariable String objectName,
                         HttpServletResponse response) throws Exception {

        var stat = minioImageService.stat(objectName);

        if (stat.contentType() == null || !stat.contentType().startsWith("image/")) {
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }

        response.setContentType(stat.contentType());
        response.setHeader("Cache-Control", "public, max-age=86400");

        try (InputStream in = minioImageService.getStream(objectName);
             OutputStream out = response.getOutputStream()) {

            in.transferTo(out);
            out.flush();
        }
    }
}
