package com.microservices.demo.runner

import com.fasterxml.jackson.annotation.JsonProperty

data class RecentChange(
    @JsonProperty("\$schema")
    val schema: String?,
    val meta: Meta?,
    val id: Long?,
    val type: String?,
    val namespace: Int?,
    val title: String?,
    @JsonProperty("title_url")
    val titleUrl: String?,
    val comment: String?,
    val timestamp: Long?,
    val user: String?,
    val bot: Boolean?,
    @JsonProperty("notify_url")
    val notifyUrl: String?,
    val minor: Boolean?,
    val patrolled: Boolean?,
    val length: Length?,
    val revision: Revision?,
    @JsonProperty("server_url")
    val serverUrl: String?,
    @JsonProperty("server_name")
    val serverName: String?,
    @JsonProperty("server_script_path")
    val serverScriptPath: String?,
    val wiki: String?,
    val parsedcomment: String?
)

data class Meta(
    val uri: String?,
    @JsonProperty("request_id")
    val requestId: String?,
    val id: String?,
    val dt: String?,
    val domain: String?,
    val stream: String?,
    val topic: String?,
    val partition: Int?,
    val offset: Int?
)

data class Length(
    val old: Int?,
    val new: Int?
)

data class Revision(
    val old: Int?,
    val new: Int?
)