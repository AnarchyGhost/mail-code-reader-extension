package com.anarchyghost.models

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val folder: String,
    val useSsl: Boolean = false,
    val searchCount: Int = 10,
    val titleRegex: String,
    val codeRegex: String,
    val searchInBody: Boolean = false,
)