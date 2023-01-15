package com.ib.eventaid.eventsnearyou.presentation

import com.ib.eventaid.common.presentation.UIEvent
import com.ib.eventaid.common.presentation.model.Event

data class EventsNearYouViewState(
    val loading: Boolean = true,
    val events: List<UIEvent> = emptyList(),
    val noMoreEventsNearby: Boolean = false,
    val failure: Event<Throwable>? = null
)
