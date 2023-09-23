package com.microservices.demo

import com.microservices.demo.runner.StreamRunnerImpl
import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

private val logger = KotlinLogging.logger {  }

@SpringBootApplication
@ConfigurationPropertiesScan
class StreamToKafkaServiceApplication(val config: StreamToKafkaServiceConfigData, val runner: StreamRunnerImpl) : CommandLineRunner {
    override fun run(vararg args: String?) {
        logger.info { "App starts..." }
        logger.info { config.baseUrl }
        logger.info { config.uri }
        runner.start()
    }
}
fun main(args: Array<String>) {
    runApplication<StreamToKafkaServiceApplication>(*args)
}
