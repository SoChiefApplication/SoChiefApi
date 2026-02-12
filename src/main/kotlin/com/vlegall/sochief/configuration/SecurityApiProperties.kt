package com.vlegall.sochief.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security.api")
data class SecurityApiProperties(
    val keys: List<String> = emptyList(),
)
