package com.microservices.demo

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka-producer-config")
data class KafkaProducerConfigData (
    val keySerializerClass: String,
    val valueSerializerClass: String,
    val compressionType: String,
    val acks: String,
    val batchSize: Int,
    val batchSizeBoostFactor: Int,
    val lingerMs: Int,
    val requestTimeoutMs: Int,
    val retryCount: Int
)