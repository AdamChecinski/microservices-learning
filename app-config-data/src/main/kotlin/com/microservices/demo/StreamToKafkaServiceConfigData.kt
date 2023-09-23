package com.microservices.demo

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "stream-to-kafka-service")
data class StreamToKafkaServiceConfigData(
    val baseUrl: String,
    val uri: String
)