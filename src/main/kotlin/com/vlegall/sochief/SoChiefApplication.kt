package com.vlegall.sochief

import com.vlegall.sochief.configuration.minio.MinioProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(MinioProperties::class)

@SpringBootApplication
class SoChiefApplication

fun main(args: Array<String>) {
    runApplication<SoChiefApplication>(*args)
}
