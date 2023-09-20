package com.microservices.demo.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "stream-to-kafka-service")
data class StreamToKafkaServiceConfigData(
    val baseUrl: String,
    val uri: String
)