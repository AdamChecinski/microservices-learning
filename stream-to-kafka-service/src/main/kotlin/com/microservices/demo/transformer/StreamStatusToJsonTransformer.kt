package com.microservices.demo.transformer

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.microservices.demo.RecentChange
import org.springframework.stereotype.Component

@Component
class StreamStatusToJsonTransformer{
    final val mapper = ObjectMapper().registerKotlinModule()
    init {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }
    fun getRecentChangeFromStatus(status: String): RecentChange {
        return mapper.readValue(status, RecentChange::class.java)
    }
}