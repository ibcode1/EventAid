package com.ib.eventaid.common.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiTaxonomy(
    @field:Json(name = "id") val id: Int?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "parent_id") val parentId: Any?,
    @field:Json(name = "rank") val rank: Int?
)