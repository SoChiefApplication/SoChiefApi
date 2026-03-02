package fr.vlegall.sochief.model.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter(autoApply = true)
public class DurationMillisConverter implements AttributeConverter<Duration, Long> {
    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        return attribute != null ? attribute.toMillis() : null;
    }

    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        return dbData != null ? Duration.ofMillis(dbData) : null;
    }
}

