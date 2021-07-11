package com.buslaev.monitoringcryptocurrency.models.news

import java.io.Serializable

data class Reference(
    val name: String,
    val url: String
) : Serializable