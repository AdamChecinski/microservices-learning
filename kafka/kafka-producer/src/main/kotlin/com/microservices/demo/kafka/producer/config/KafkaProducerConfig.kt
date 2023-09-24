package com.microservices.demo.kafka.producer.config

import com.microservices.demo.KafkaConfigData
import com.microservices.demo.KafkaProducerConfigData
import com.microservices.demo.RecentChange
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import java.io.Serializable

@Configuration
@EnableConfigurationProperties(KafkaConfigData::class, KafkaProducerConfigData::class)
class KafkaProducerConfig<K,V> (
    val kafkaConfigData: KafkaConfigData,
    val kafkaProducerConfig: KafkaProducerConfigData
) where K: Serializable, V: RecentChange {
    @Bean
    fun producerConfig(): Map<String, Any> = hashMapOf<String, Any>(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaConfigData.bootstrapServers,
        kafkaConfigData.schemaRegistryUrlKey to kafkaConfigData.schemaRegistryUrl,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to kafkaProducerConfig.keySerializerClass,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to kafkaProducerConfig.valueSerializerClass,
        ProducerConfig.BATCH_SIZE_CONFIG to kafkaProducerConfig.batchSize * kafkaProducerConfig.batchSizeBoostFactor,
        ProducerConfig.LINGER_MS_CONFIG to kafkaProducerConfig.lingerMs,
        ProducerConfig.COMPRESSION_TYPE_CONFIG to kafkaProducerConfig.compressionType,
        ProducerConfig.ACKS_CONFIG to kafkaProducerConfig.acks,
        ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG to kafkaProducerConfig.requestTimeoutMs,
        ProducerConfig.RETRIES_CONFIG to kafkaProducerConfig.retryCount
    )

    @Bean
    fun producerFactory(): ProducerFactory<K, V> = DefaultKafkaProducerFactory(producerConfig())

    @Bean
    fun kafkaTemplate(): KafkaTemplate<K, V> = KafkaTemplate(producerFactory())
}