package com.vlegall.sochief.configuration.minio

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import jakarta.annotation.PostConstruct

import org.springframework.stereotype.Component

@Component
class MinioInitializer(
    private val minioClient: MinioClient,
    private val minioProperties: MinioProperties
) {
    @PostConstruct
    fun init() = with(minioProperties) {
        val bucket = bucket
        require(bucket.isNotBlank()) { "MINIO_BUCKET is not configured" }

        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build())
            }
        } catch (e: Exception) {
            throw IllegalStateException("Cannot initialize MinIO bucket", e)
        }
    }


}