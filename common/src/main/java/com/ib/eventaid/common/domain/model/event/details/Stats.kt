package com.ib.eventaid.common.domain.model.event.details

data class Stats(
    val averagePrice:Int,
    val highestPrice:Int,
    val listingCount:Int,
    val lowestPrice:Int,
    val medianPrice: Int,
    val visibleListingCount: Int,
    val lowestPriceGoodDeals: Int,
    val lowestSgBasePrice: Int,
    val lowestSgBasePriceGoodDeals: Int,
)
