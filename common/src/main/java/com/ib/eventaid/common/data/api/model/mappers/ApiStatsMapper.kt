package com.ib.eventaid.common.data.api.model.mappers

import com.ib.eventaid.common.data.api.model.ApiStats
import com.ib.eventaid.common.domain.model.event.details.Stats
import javax.inject.Inject

class ApiStatsMapper @Inject constructor() : ApiMapper<ApiStats?, Stats> {
    override fun mapToDomain(apiEntity: ApiStats?): Stats {
        return Stats(
            averagePrice = apiEntity?.averagePrice ?: 0,
            highestPrice = apiEntity?.highestPrice ?: 0,
            listingCount = apiEntity?.listingCount ?: 0,
            lowestPrice = apiEntity?.lowestPrice ?: 0,
            lowestPriceGoodDeals = apiEntity?.lowestPriceGoodDeals ?: 0,
            lowestSgBasePrice = apiEntity?.lowestSgBasePrice ?: 0,
            lowestSgBasePriceGoodDeals = apiEntity?.lowestSgBasePriceGoodDeals ?: 0,
            medianPrice = apiEntity?.medianPrice ?: 0,
            visibleListingCount = apiEntity?.visibleListingCount ?: 0
        )
    }
}