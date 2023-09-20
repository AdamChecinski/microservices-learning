package com.microservices.demo.runner

import com.fasterxml.jackson.databind.ObjectMapper
import com.microservices.demo.config.StreamToKafkaServiceConfigData
import jakarta.annotation.PreDestroy
import mu.KotlinLogging
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.Disposable
import reactor.core.publisher.Flux
import java.time.LocalTime

private val logger = KotlinLogging.logger {  }

/**
 *
 * @author checa
 * @version $ 20/09/2023
 */

@Component
class StreamRunnerImpl(
    config: StreamToKafkaServiceConfigData,
    val mapper: ObjectMapper
) : StreamRunner {
    private lateinit var eventStream: Flux<ServerSentEvent<String>>
    private var disposable: Disposable? = null
//TODO("DODAJ MOCK STREAM RUNNERA DLA TYCH DANYCH :---))))")
    init {
        val client: WebClient = WebClient.create(config.baseUrl)
        val type: ParameterizedTypeReference<ServerSentEvent<String>> = object : ParameterizedTypeReference<ServerSentEvent<String>> () {}
        eventStream = client.get()
            .uri(config.uri)
            .retrieve()
            .bodyToFlux(type)
    }

    override fun start() {
        disposable = eventStream.subscribe(
            { content ->
                if (content.data() != null) {
                    val readValue = mapper.readValue(content.data(), RecentChange::class.java)
                    logger.info {
                        "\n\n" +
                        mapper.readTree(content.data()).toPrettyString() +
                        "\n\n"
                    }
                } else {
                    logger.info(
                        "Time: {} - event: name[{}], id [{}], content[{}] ",
                        LocalTime.now(), content.event(), content.id(), content.data()
                    )
                }
            },
            { error -> logger.error("Error receiving SSE: {}", error) }
        ) { logger.info("Completed!!!") }
    }

    @PreDestroy
    fun shutdown() {
        logger.info { "Calling dispose()... (isDisposed:${disposable?.isDisposed})" }
        disposable?.dispose()
        logger.info { "Disposed (isDisposed:${disposable?.isDisposed})" }
    }
}