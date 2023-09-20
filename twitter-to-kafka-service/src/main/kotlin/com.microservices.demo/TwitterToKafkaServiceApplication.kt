package com.microservices.demo

import com.microservices.demo.config.TwitterToKafkaServiceConfigData
import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

private val logger = KotlinLogging.logger {  }

@SpringBootApplication
@ConfigurationPropertiesScan
class TwitterToKafkaServiceApplication(val config: TwitterToKafkaServiceConfigData) : CommandLineRunner {
    override fun run(vararg args: String?) {
        logger.info { "App starts..." }
        logger.info { config.welcomeMessage }
        logger.info { config.twitterKeywords.toString() }
    }
}
fun main(args: Array<String>) {
    runApplication<TwitterToKafkaServiceApplication>(*args)
}
