package com.ib.eventaid.common.data.api.model.oauth


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meta(
    @field:Json(name = "status") val status: Int
)