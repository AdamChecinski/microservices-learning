package com.microservices.demo.init

import com.microservices.demo.KafkaConfigData
import com.microservices.demo.kafka.admin.client.KafkaAdminClient
import mu.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {  }

@Component
class StreamInitializerImpl(
    val kafkaConfigData: KafkaConfigData,
    val kafkaAdminClient: KafkaAdminClient
): StreamInitializer {
    override fun init() {
        kafkaAdminClient.createTopics()
        kafkaAdminClient.checkSchemaRegistry()
        logger.info { "Topics with name ${kafkaConfigData.topicNamesToCreate} are ready for operations!" }
    }
}