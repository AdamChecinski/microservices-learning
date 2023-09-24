package com.microservices.demo

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "retry-config")
data class RetryConfigData @ConstructorBinding constructor (
    val initialIntervalMillis: Long,
    val maxIntervalMillis: Long,
    val multiplier: Double,
    val maxAttempts: Int,
    val sleepTimeMillis: Long,
)