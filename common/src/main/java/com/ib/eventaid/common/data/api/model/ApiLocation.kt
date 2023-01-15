package com.ib.eventaid.common.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiLocation(
    @field:Json(name = "lat") val lat: Double?,
    @field:Json(name = "lon") val lon: Double?
)