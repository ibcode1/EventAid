package com.ib.eventaid.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiEventPromotion(
    @field:Json(name = "additional_info") val additionalInfo: String?,
    @field:Json(name = "headline") val headline: String?,
    @field:Json(name = "images") val images: ApiImageSizes?
)