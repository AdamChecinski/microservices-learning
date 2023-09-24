package com.microservices.demo.kafka.producer.config.service

import com.microservices.demo.RecentChange
import jakarta.annotation.PreDestroy
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

private val logger = KotlinLogging.logger {  }

@Component
class StreamKafkaProducer(
    val kafkaTemplate: KafkaTemplate<Long, RecentChange>
): KafkaProducer<Long, RecentChange> {
    override fun send(topicName: String, key: Long, message: RecentChange) {
        logger.info { "Sending message={$message} to topic {$topicName}" }
        val kafkaResultFuture = kafkaTemplate.send(topicName, key, message)
        addCallback(kafkaResultFuture, message, topicName)
    }

    @PreDestroy
    fun close() {
        logger.info { "Closing kafka producer" }
        kafkaTemplate.destroy()
    }

    private fun addCallback(
        kafkaResultFuture: CompletableFuture<SendResult<Long, RecentChange>>,
        message: RecentChange,
        topicName: String
    ) {
        kafkaResultFuture.whenComplete { result, throwable ->
            if (null != throwable) {
                logger.error(throwable) { "Error while sending message {$message} to topic {$topicName}" }
            } else {
                val metadata = result.recordMetadata
                logger.debug {
                    "Received new metadata. Topic: {${metadata.topic()}}; Partition: {${metadata.partition()}};" +
                            "Offset: {${metadata.offset()}}; Timestamp: ${metadata.timestamp()}; at a time {${System.nanoTime()}}"
                }
            }
        }
    }
}