package com.ib.eventaid.common.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiEvent(
    @field:Json(name = "access_method") val accessMethod: Any?,
    @field:Json(name = "announce_date") val announceDate: String?,
    @field:Json(name = "announcements") val announcements: ApiAnnouncements?,
    @field:Json(name = "approved") val approved: Boolean?,
    @field:Json(name = "archived") val archived: Boolean?,
    @field:Json(name = "away_team") val awayTeam: Any?,
    @field:Json(name = "conditional") val conditional: Boolean?,
    @field:Json(name = "created_at") val createdAt: String?,
    @field:Json(name = "date_tbd") val dateTbd: Boolean?,
    @field:Json(name = "datetime_local") val datetimeLocal: String?,
    @field:Json(name = "datetime_tbd") val datetimeTbd: Boolean?,
    @field:Json(name = "datetime_utc") val datetimeUtc: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "domain_information") val domainInformation: List<Any>?,
    @field:Json(name = "enddatetime_utc") val enddatetimeUtc: Any?,
    @field:Json(name = "event_promotion") val eventPromotion: ApiEventPromotion?,
    @field:Json(name = "general_admission") val generalAdmission: Boolean?,
    @field:Json(name = "home_team") val homeTeam: Any?,
    @field:Json(name = "id") val id: Int?,
    @field:Json(name = "is_open") val isOpen: Boolean?,
    @field:Json(name = "links") val links: List<Any>?,
    @field:Json(name = "performers") val performers: List<ApiPerformer>?,
    @field:Json(name = "popularity") val popularity: Double?,
    @field:Json(name = "score") val score: Double?,
    @field:Json(name = "short_title") val shortTitle: String?,
    @field:Json(name = "stats") val stats: ApiStats?,
    @field:Json(name = "taxonomies") val taxonomies: List<ApiTaxonomy>?,
    @field:Json(name = "taxonomy_id") val taxonomyId: Int?,
    @field:Json(name = "themes") val themes: List<Any>?,
    @field:Json(name = "time_tbd") val timeTbd: Boolean?,
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "type") val type: String?,
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "venue") val venue: ApiVenue?,
    @field:Json(name = "visible_until_utc") val visibleUntilUtc: String?
)