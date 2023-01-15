package com.ib.eventaid.common.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiVenue(
    @field:Json(name = "access_method") val accessMethod: Any?,
    @field:Json(name = "address") val address: String?,
    @field:Json(name = "capacity") val capacity: Int?,
    @field:Json(name = "city") val city: String?,
    @field:Json(name = "country") val country: String?,
    @field:Json(name = "display_location") val displayLocation: String?,
    @field:Json(name = "extended_address") val extendedAddress: String?,
    @field:Json(name = "has_upcoming_events") val hasUpcomingEvents: Boolean?,
    @field:Json(name = "id") val id: Int?,
    @field:Json(name = "links") val links: List<Any>?,
    @field:Json(name = "location") val location: ApiLocation?,
    @field:Json(name = "metro_code") val metroCode: Int?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "name_v2") val nameV2: String?,
    @field:Json(name = "num_upcoming_events") val numUpcomingEvents: Int?,
    @field:Json(name = "popularity") val popularity: Int?,
    @field:Json(name = "postal_code") val postalCode: String?,
    @field:Json(name = "score") val score: Double?,
    @field:Json(name = "slug") val slug: String?,
    @field:Json(name = "state") val state: String?,
    @field:Json(name = "timezone") val timezone: String?,
    @field:Json(name = "url") val url: String?
)