package com.microservices.demo.kafka.admin.exception

class KafkaClientException: RuntimeException {
    constructor(message: String, t: Throwable): super(message, t)
    constructor(message: String): super(message)
}