package com.microservices.demo.kafka.admin.config

import com.microservices.demo.KafkaConfigData
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.admin.AdminClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(KafkaConfigData::class)
class KafkaAdminConfig (val kafkaConfigData: KafkaConfigData) {
    @Bean
    fun adminClient(): AdminClient =
        AdminClient.create(mapOf(
            CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG to kafkaConfigData.bootstrapServers
        ))

}