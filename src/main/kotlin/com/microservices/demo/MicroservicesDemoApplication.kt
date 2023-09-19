package com.microservices.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MicroservicesDemoApplication

fun main(args: Array<String>) {
    runApplication<MicroservicesDemoApplication>(*args)
}
