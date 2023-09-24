package com.microservices.demo.common.config

import com.microservices.demo.RetryConfigData
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

@Configuration
class RetryConfig (val retryConfigData: RetryConfigData) {
    @Bean
    fun retryTemplate(): RetryTemplate {
        val retryTemplate = RetryTemplate()
        setupBackoffPolicy(retryTemplate)
        setupRetryPolicy(retryTemplate)
        return retryTemplate
    }

    private fun setupRetryPolicy(retryTemplate: RetryTemplate) {
        val simpleRetryPolicy = SimpleRetryPolicy()
        simpleRetryPolicy.maxAttempts = retryConfigData.maxAttempts
        retryTemplate.setRetryPolicy(simpleRetryPolicy)
    }

    private fun setupBackoffPolicy(retryTemplate: RetryTemplate) {
        val exponentialBackOffPolicy = ExponentialBackOffPolicy()
        exponentialBackOffPolicy.initialInterval = retryConfigData.initialIntervalMillis
        exponentialBackOffPolicy.maxInterval = retryConfigData.maxIntervalMillis
        exponentialBackOffPolicy.multiplier = retryConfigData.multiplier
        exponentialBackOffPolicy.multiplier = retryConfigData.multiplier
        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy)
    }
}