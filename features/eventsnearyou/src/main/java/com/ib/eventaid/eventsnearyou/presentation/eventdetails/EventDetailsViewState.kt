package com.ib.eventaid.eventsnearyou.presentation.eventdetails

import com.ib.eventaid.eventsnearyou.presentation.eventdetails.model.UIEventDetailed

sealed class EventDetailsViewState {
    object Loading : EventDetailsViewState()

    data class EventDetails(
        val event: UIEventDetailed
    ) : EventDetailsViewState()

    object Failure : EventDetailsViewState()

}