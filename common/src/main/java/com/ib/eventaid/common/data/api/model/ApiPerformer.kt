package com.ib.eventaid.common.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPerformer(
    @field:Json(name = "away_team") val awayTeam: Boolean?,
    @field:Json(name = "colors") val colors: Any?,
    @field:Json(name = "divisions") val divisions: Any?,
    @field:Json(name = "genres") val genres: List<ApiGenre>?,
    @field:Json(name = "has_upcoming_events") val hasUpcomingEvents: Boolean?,
    @field:Json(name = "home_team") val homeTeam: Boolean?,
    @field:Json(name = "home_venue_id") val homeVenueId: Any?,
    @field:Json(name = "id") val id: Int?,
    @field:Json(name = "image") val image: String?,
    @field:Json(name = "image_attribution") val imageAttribution: String?,
    @field:Json(name = "image_license") val imageLicense: String?,
    @field:Json(name = "image_rights_message") val imageRightsMessage: String?,
    @field:Json(name = "images") val images: ApiImageSizes?,
    @field:Json(name = "location") val location: Any?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "num_upcoming_events") val numUpcomingEvents: Int?,
    @field:Json(name = "popularity") val popularity: Int?,
    @field:Json(name = "primary") val primary: Boolean?,
    @field:Json(name = "score") val score: Double?,
    @field:Json(name = "short_name") val shortName: String?,
    @field:Json(name = "slug") val slug: String?,
    @field:Json(name = "stats") val stats: ApiStatsUpcomingEvents?,
    @field:Json(name = "status") val status: String?,
    @field:Json(name = "taxonomies") val taxonomies: List<ApiTaxonomy>?,
    @field:Json(name = "type") val type: String?,
    @field:Json(name = "url") val url: String?,
)