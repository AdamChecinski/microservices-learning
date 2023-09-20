package com.microservices.demo.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "twitter-to-kafka-service")
data class TwitterToKafkaServiceConfigData(
    val twitterKeywords: List<String>,
    val welcomeMessage: String
)