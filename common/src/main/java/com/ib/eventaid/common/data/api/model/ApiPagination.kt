package com.ib.eventaid.common.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPagination(
    @field:Json(name = "geolocation") val geolocation: ApiGeolocation?,
    @field:Json(name = "page") val page: Int?,
    @field:Json(name = "per_page") val perPage: Int?,
    @field:Json(name = "took") val took: Int?,
    @field:Json(name = "total") val total: Int?
)