package com.ib.eventaid.common.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiImageSizes(
    @field:Json(name = "banner") val banner: String?,
    @field:Json(name = "block") val block: String?,
    @field:Json(name = "criteo_130_160") val criteo130160: String?,
    @field:Json(name = "criteo_170_235") val criteo170235: String?,
    @field:Json(name = "criteo_205_100") val criteo205100: String?,
    @field:Json(name = "criteo_400_300") val criteo400300: String?,
    @field:Json(name = "fb_100x72") val fb100x72: String?,
    @field:Json(name = "fb_600_315") val fb600315: String?,
    @field:Json(name = "huge") val huge: String?,
    @field:Json(name = "ipad_event_modal") val ipadEventModal: String?,
    @field:Json(name = "ipad_header") val ipadHeader: String?,
    @field:Json(name = "ipad_mini_explore") val ipadMiniExplore: String?,
    @field:Json(name = "mongo") val mongo: String?,
    @field:Json(name = "square_mid") val squareMid: String?,
    @field:Json(name = "triggit_fb_ad") val triggitFbAd: String?,
    @field:Json(name = "136x136") val x136: String?,
    @field:Json(name = "800x320") val x320: String?,
    @field:Json(name = "500_700") val x500700: String?,
    @field:Json(name = "1200x525") val x525: String?,
    @field:Json(name = "1200x627") val x627: String?
)