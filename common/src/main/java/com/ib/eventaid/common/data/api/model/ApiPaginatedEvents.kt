package com.ib.eventaid.common.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPaginatedEvents(
    @field:Json(name = "events") val events: List<ApiEvent>?,
    @field:Json(name = "meta") val meta: ApiPagination?
)