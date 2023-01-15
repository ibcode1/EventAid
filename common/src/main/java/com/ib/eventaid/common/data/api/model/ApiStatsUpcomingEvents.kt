package com.ib.eventaid.common.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiStatsUpcomingEvents(
    @Json(name = "event_count") val eventCount: Int?
)