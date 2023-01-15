package com.ib.eventaid.common.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiStats(
    @field:Json(name = "average_price") val averagePrice: Int?,
    @field:Json(name = "dq_bucket_counts") val dqBucketCounts: List<Any>?,
    @field:Json(name = "highest_price") val highestPrice: Int?,
    @field:Json(name = "listing_count") val listingCount: Int?,
    @field:Json(name = "lowest_price") val lowestPrice: Int?,
    @field:Json(name = "lowest_price_good_deals") val lowestPriceGoodDeals: Int?,
    @field:Json(name = "lowest_sg_base_price") val lowestSgBasePrice: Int?,
    @field:Json(name = "lowest_sg_base_price_good_deals") val lowestSgBasePriceGoodDeals: Int?,
    @field:Json(name = "median_price") val medianPrice: Int?,
    @field:Json(name = "visible_listing_count") val visibleListingCount: Int?
)