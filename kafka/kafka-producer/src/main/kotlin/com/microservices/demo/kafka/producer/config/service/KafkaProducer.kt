package com.microservices.demo.kafka.producer.config.service

import com.microservices.demo.RecentChange
import java.io.Serializable

interface KafkaProducer<K,V> where K: Serializable, V: RecentChange {
    fun send(topicName: String, key: K, message: V)
}