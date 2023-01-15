package com.ib.eventaid.common.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiGenre(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "primary") val primary: Boolean,
    @Json(name = "slug") val slug: String
)