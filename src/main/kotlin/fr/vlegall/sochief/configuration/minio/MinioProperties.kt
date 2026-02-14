package fr.vlegall.sochief.configuration.minio

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "minio")
data class MinioProperties(
    val url: String,
    val accessKey: String,
    val secretKey: String,
    val bucket: String
)