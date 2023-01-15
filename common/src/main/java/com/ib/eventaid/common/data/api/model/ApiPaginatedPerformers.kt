package com.ib.eventaid.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPaginatedPerformers(
    @field:Json(name = "performers") val performers: List<ApiPerformer>?,
    @field:Json(name = "meta") val meta: ApiPagination?
)
