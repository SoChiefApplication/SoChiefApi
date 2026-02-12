package com.vlegall.sochief

import com.vlegall.sochief.configuration.minio.MinioProperties
import com.vlegall.sochief.configuration.SecurityApiProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(
    value = [
        MinioProperties::class,
        SecurityApiProperties::class
    ]
)
@SpringBootApplication
class SoChiefApplication

fun main(args: Array<String>) {
    runApplication<SoChiefApplication>(*args)
}
