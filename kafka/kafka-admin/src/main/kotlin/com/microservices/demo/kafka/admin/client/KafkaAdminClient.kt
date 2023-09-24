package com.microservices.demo.kafka.admin.client

import com.microservices.demo.KafkaConfigData
import com.microservices.demo.RetryConfigData
import com.microservices.demo.common.config.RetryConfig
import com.microservices.demo.kafka.admin.exception.KafkaClientException
import mu.KotlinLogging
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.CreateTopicsResult
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.admin.TopicListing
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.retry.RetryContext
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger {  }

@Component
class KafkaAdminClient (
    val kafkaConfigData: KafkaConfigData,
    val retryConfigData: RetryConfigData,
    val adminClient: AdminClient,
    val retryTemplate: RetryTemplate,
    val k: RetryConfig,
    val webClient: WebClient
) {
    fun createTopics() {
        try {
            retryTemplate.execute<CreateTopicsResult, Throwable>(this::doCreateTopics)
        } catch (t: Throwable) {
            throw KafkaClientException("Reached max number of retry for creating kafka topic(s)!", t)
        }
        checkTopicsCreated()
        logger.info { kafkaConfigData.bootstrapServers }
    }

    fun checkTopicsCreated() {
        var topics = getTopics()
        val topicsToCreate = kafkaConfigData.topicNamesToCreate
        val maxRetry = retryConfigData.maxAttempts
        val multiplier = retryConfigData.multiplier
        var sleepTimeMillis = retryConfigData.sleepTimeMillis
        var retryCount = 1

        topicsToCreate.forEach {
            while (!isTopicCreated(topics, it)) {
                checkMaxRetry(retryCount++, maxRetry)
                sleep(sleepTimeMillis)
                sleepTimeMillis = (multiplier * sleepTimeMillis).toLong()
                topics = getTopics()
            }
        }
    }

    fun checkSchemaRegistry() {
        val maxRetry = retryConfigData.maxAttempts
        val multiplier = retryConfigData.multiplier
        var sleepTimeMillis = retryConfigData.sleepTimeMillis
        var retryCount = 1
        while (!getSchemaRegistryStatus().is2xxSuccessful) {
            checkMaxRetry(retryCount++, maxRetry)
            sleep(sleepTimeMillis)
            sleepTimeMillis = (multiplier * sleepTimeMillis).toLong()
        }
    }

    private fun getSchemaRegistryStatus(): HttpStatus =
        webClient.method(HttpMethod.GET).uri(kafkaConfigData.schemaRegistryUrl).exchangeToMono {
            Mono.just(it.statusCode() as HttpStatus)
        }.doOnError {
            HttpStatus.SERVICE_UNAVAILABLE
        }.block()!!

    private fun sleep(sleepTimeMillis: Long) {
        try {
            Thread.sleep(sleepTimeMillis)
        } catch (exception: InterruptedException) {
            throw KafkaClientException("Error while waiting for new created topics!")
        }
    }

    private fun checkMaxRetry(retryCount: Int, maxRetry: Int) {
        if (retryCount > maxRetry) {
            throw KafkaClientException("Reached max number of retry for reading kafka topic(s)!")
        }
    }

    private fun isTopicCreated(topics: Collection<TopicListing>, topicName: String): Boolean =
        topics.stream().anyMatch {it.name().equals(topicName)}

    private fun doCreateTopics(retryContext: RetryContext): CreateTopicsResult {
        val topicNamesToCreate = kafkaConfigData.topicNamesToCreate
        logger.info { "Creating ${topicNamesToCreate.size} topic(s), attempt ${retryContext.retryCount}" }
        val kafkaTopics = topicNamesToCreate.stream()
            .map { NewTopic(it.trim(), kafkaConfigData.numberOfPartitions, kafkaConfigData.replicationFactor) }.toList()
        return adminClient.createTopics(kafkaTopics)
    }

    private fun getTopics(): Collection<TopicListing> {
        try {
            return retryTemplate.execute<Collection<TopicListing>, Throwable>(this::doGetTopics)
        } catch (t: Throwable) {
            throw KafkaClientException("Reached max number of retry for reading kafka topic(s)!", t)
        }
    }

    private fun doGetTopics(retryContext: RetryContext): Collection<TopicListing> {
        logger.info { "Reading kafka topics ${kafkaConfigData.topicNamesToCreate}, attempt ${retryContext.retryCount}" }
        return adminClient.listTopics().listings().get().onEach {
            logger.debug { "Topic with name ${it.name()}" }
        }
    }

}