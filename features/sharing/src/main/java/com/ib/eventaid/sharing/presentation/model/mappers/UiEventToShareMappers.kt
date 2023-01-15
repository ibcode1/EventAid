package com.ib.eventaid.sharing.presentation.model.mappers

import com.ib.eventaid.common.domain.model.event.details.EventWithDetails
import com.ib.eventaid.common.presentation.mappers.UiMapper
import com.ib.eventaid.sharing.presentation.model.UIEventToShare
import javax.inject.Inject

class UiEventToShareMappers @Inject constructor():UiMapper<EventWithDetails, UIEventToShare> {
    override fun mapToView(input: EventWithDetails): UIEventToShare {
        val message = createMessage(input)
        return UIEventToShare(
            input.media.getFirstSmallestAvailableImage(),message,
        )
    }

    private fun createMessage(input: EventWithDetails):String{
        val details = input.details
        val description=details.description
        val venue = details.venue.name
        val address = details.venue.address.address1
        val performers = details.performers.map { it.name }
        val price = details.stats.averagePrice

        return "${description}\n\nVenue info:\n$venue\n\n$address\n\nPerformers:\n$performers\n\nPrice:\n${"$$price"}"
    }
}
