package com.ib.eventaid.performers.presentation.performerdetails

import com.ib.eventaid.common.presentation.UIEvent
import com.ib.eventaid.common.presentation.UIPerformerEvents
import com.ib.eventaid.common.presentation.model.Event
import com.ib.eventaid.performers.presentation.performerdetails.model.UIPerformerDetailed
import io.reactivex.Flowable

sealed class PerformerDetailsViewState {
    object Loading : PerformerDetailsViewState()

    data class PerformerDetails(
        val performer:UIPerformerDetailed,
       // val event:List<UIEvent> = emptyList()
    ):PerformerDetailsViewState()

    object Failure : PerformerDetailsViewState()
}


data class PerformerEventsViewState(
    val loading: Boolean = true,
    val uiPerformerEvents: List<UIPerformerEvents> = emptyList(),
    val noMoreEvents: Boolean = false,
    val failure: Event<Throwable>? = null
)

