package com.ib.eventaid.common.presentation.mappers

import com.ib.eventaid.common.domain.model.event.Event
import com.ib.eventaid.common.presentation.UIEvent
import javax.inject.Inject

class UiEventMapper @Inject constructor():UiMapper<Event, UIEvent>{
    override fun mapToView(input: Event): UIEvent {
        return UIEvent(
            id=input.id,
            title =input.title,
            image = input.media.getFirstSmallestAvailableImage()
        )
    }
}