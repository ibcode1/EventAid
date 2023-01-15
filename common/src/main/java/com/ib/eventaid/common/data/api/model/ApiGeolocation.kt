package com.ib.eventaid.common.data.api.model
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiGeolocation(
    @field:Json(name = "city") val city: String,
    @field:Json(name = "country") val country: String,
    @field:Json(name = "display_name") val displayName: String,
    @field:Json(name = "lat") val lat: Double,
    @field:Json(name = "lon") val lon: Double,
    @field:Json(name = "metro_code") val metroCode: String,
    @field:Json(name = "postal_code") val postalCode: String,
    @field:Json(name = "range") val range: String,
    @field:Json(name = "state") val state: String
)