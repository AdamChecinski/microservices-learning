package com.microservices.demo.runner

import com.microservices.demo.StreamToKafkaServiceConfigData
import com.microservices.demo.listener.KafkaStatusListener
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

@Component
class StreamRunnerImpl(
    serviceConfig: StreamToKafkaServiceConfigData,
    val listener: KafkaStatusListener
) : StreamRunner {
    private lateinit var eventStream: Flux<ServerSentEvent<String>>
    private var disposable: Disposable? = null
//TODO("DODAJ MOCK STREAM RUNNERA DLA TYCH DANYCH :---))))")
    init {
        val client: WebClient = WebClient.create(serviceConfig.baseUrl)
        val type: ParameterizedTypeReference<ServerSentEvent<String>> = object : ParameterizedTypeReference<ServerSentEvent<String>>() {}
        eventStream = client.get()
            .uri(serviceConfig.uri)
            .retrieve()
            .bodyToFlux(type)
    }

    override fun start() {
        disposable = eventStream.subscribe(
            { content ->
                val data: String? = content.data()
                if (data != null) {
                    listener.onStatus(data)
                } else {
                    logger.error {
                        "Stream data is null. Time: {${LocalTime.now()}} - event: name[{${content.event()}}], id [{${content.id()}}], content[{${content.data()}}] "
                    }
                }
            },
            { error -> logger.error(error) {"Error receiving SSE: {}"} }
        ) { logger.info {"Completed!!!"} }
    }

    @PreDestroy
    fun shutdown() {
        logger.info { "Calling dispose()... (isDisposed:${disposable?.isDisposed})" }
        disposable?.dispose()
        logger.info { "Disposed (isDisposed:${disposable?.isDisposed})" }
    }
}