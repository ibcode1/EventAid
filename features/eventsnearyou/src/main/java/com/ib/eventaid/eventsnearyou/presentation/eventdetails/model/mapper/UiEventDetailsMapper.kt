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
            performers = input.details.performers,
            description = input.details.description,
            averagePrice = input.details.stats.averagePrice,
            highestPrice = input.details.stats.highestPrice,
            listingCount = input.details.stats.listingCount,
            lowestPrice = input.details.stats.lowestPrice,
            medianPrice = input.details.stats.medianPrice,
            image = input.media.getFirstSmallestAvailableImage(),
            //taxonomy = input.details.taxonomy.map { it.name }
        )
    }

    /*private fun parsePerformer(apiPerformer: EventWithDetails):Performer{
        return Performer(
            awayTeam = apiPerformer.details.performers.isEmpty(),
            hasUpcomingEvents = apiPerformer.details.performers.isEmpty(),
            homeTeam = apiPerformer.details.performers.isEmpty(),
            id = apiPerformer.details.performers.size,
            image = apiPerformer.details.performers.toString(),
            name = apiPerformer.details.performers.toString(),
            numUpcomingEvents = apiPerformer.details.performers.size,
            popularity = apiPerformer.details.performers.size,
            score = apiPerformer.details.performers.size.toDouble(),
            slug = apiPerformer.details.performers.toString(),
            type = apiPerformer.details.performers.toString(),
            url = apiPerformer.details.performers.toString()
        )
    }*/
}