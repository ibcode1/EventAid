package com.ib.eventaid.eventsnearyou.presentation.eventdetails.model.mapper

import com.ib.eventaid.common.domain.model.event.details.EventWithDetails
import com.ib.eventaid.common.presentation.mappers.UiMapper
import com.ib.eventaid.eventsnearyou.presentation.eventdetails.model.UIEventDetailed
import javax.inject.Inject

class UiEventDetailsMapper @Inject constructor() :UiMapper<EventWithDetails,UIEventDetailed> {
    override fun mapToView(input: EventWithDetails): UIEventDetailed {
        return UIEventDetailed(
            id = input.id,
            title = input.title,
            description = input.details.description,
            performer = input.details.performers,
            date = input.dateTimeLocal,
            type = listOf(input.type),
            city = input.details.venue.address.city,
            capacity = input.details.venue.info.capacity,
            score = input.details.venue.info.venueScore,
//            highestPrice = input.details.stats.highestPrice,
//            listingCount = input.details.stats.listingCount,
//            lowestPrice = input.details.stats.lowestPrice,
//            medianPrice = input.details.stats.medianPrice,
            image = input.media.getFirstSmallestAvailableImage(),
            venue = input.details.venue.name,
            taxonomy = input.details.taxonomy.map { it.name },
            address = input.details.venue.address.address1 + ", "+ input.details.venue.address.address2,
            price = "$"+input.details.stats.lowestSgBasePriceGoodDeals.toString() + " - " + "$"+input.details.stats.highestPrice.toString()
        )
    }
}