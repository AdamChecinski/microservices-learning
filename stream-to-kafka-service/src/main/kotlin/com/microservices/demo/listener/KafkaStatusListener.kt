package com.microservices.demo.listener

import com.microservices.demo.KafkaConfigData
import com.microservices.demo.RecentChange
import com.microservices.demo.kafka.producer.config.service.KafkaProducer
import com.microservices.demo.transformer.StreamStatusToJsonTransformer
import mu.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {  }

@Component
class KafkaStatusListener(
    val kafkaConfigData: KafkaConfigData,
    val kafkaProducer: KafkaProducer<Long, RecentChange>,
    val statusToJsonTransformer: StreamStatusToJsonTransformer
) {
    fun onStatus(status: String) {
        val recentChange = statusToJsonTransformer.getRecentChangeFromStatus(status)
        logger.info { "Received status with id ${recentChange.id} sending to kafka topic ${kafkaConfigData.topicName}" }
        kafkaProducer.send(
            topicName = kafkaConfigData.topicName,
            key = recentChange.user.hashCode().toLong(),
            message = recentChange
        )
    }
}