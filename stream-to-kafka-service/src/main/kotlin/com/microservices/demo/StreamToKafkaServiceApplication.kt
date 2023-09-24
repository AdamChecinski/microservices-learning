package com.microservices.demo

import com.microservices.demo.init.StreamInitializer
import com.microservices.demo.runner.StreamRunnerImpl
import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

private val logger = KotlinLogging.logger {  }

@SpringBootApplication
@ConfigurationPropertiesScan
class StreamToKafkaServiceApplication(
    val runner: StreamRunnerImpl,
    val streamInitializer: StreamInitializer
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        logger.info { "App starts..." }
        streamInitializer.init()
        runner.start()
    }
}
fun main(args: Array<String>) {
    runApplication<StreamToKafkaServiceApplication>(*args)
}
