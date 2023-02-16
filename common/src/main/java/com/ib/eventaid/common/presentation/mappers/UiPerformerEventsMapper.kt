package com.ib.eventaid.common.presentation.mappers

import com.ib.eventaid.common.domain.model.event.Event
import com.ib.eventaid.common.presentation.UIPerformerEvents
import javax.inject.Inject

class UiPerformerEventsMapper @Inject constructor():UiMapper<Event, UIPerformerEvents>{
    override fun mapToView(input: Event): UIPerformerEvents {
        return UIPerformerEvents(
            id = input.id,
            title = input.title,
            image = input.media.getFirstSmallestAvailableImage(),
        )
    }
}