package com.vlegall.sochief.model.common

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.Duration


@Converter(autoApply = true)
class DurationMillisConverter : AttributeConverter<Duration, Long> {
    override fun convertToDatabaseColumn(attribute: Duration?): Long? =
        attribute?.toMillis()

    override fun convertToEntityAttribute(dbData: Long?): Duration? =
        dbData?.let { Duration.ofMillis(it) }
}