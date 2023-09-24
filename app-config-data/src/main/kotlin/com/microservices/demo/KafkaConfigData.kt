package com.microservices.demo

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka-config")
data class KafkaConfigData (
    val bootstrapServers: String,
    val schemaRegistryUrlKey: String,
    val schemaRegistryUrl: String,
    val topicName: String,
    val topicNamesToCreate: List<String>,
    val numberOfPartitions: Int,
    val replicationFactor: Short,
)