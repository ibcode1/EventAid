package com.ib.eventaid.common.data.api.model.oauth


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class res(
    @field:Json(name = "access_token") val accessToken: String,
    @field:Json(name = "expires") val expires: Int,
    @field:Json(name = "meta") val meta: Meta
)