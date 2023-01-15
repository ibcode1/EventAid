package com.ib.eventaid.eventsnearyou.presentation.eventdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ib.eventaid.common.domain.model.event.details.EventWithDetails
import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.domain.repositories.EventRepository
import com.ib.eventaid.common.domain.usecases.GetEventDetails
import com.ib.eventaid.common.domain.usecases.GetPerformerDetails
import com.ib.eventaid.common.presentation.mappers.UiEventPerformerMapper
import com.ib.eventaid.common.utils.DispatchersProvider
import com.ib.eventaid.eventsnearyou.presentation.eventdetails.model.mapper.UiEventDetailsMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsFragmentViewModel @Inject constructor(
    private val uiEventDetailsMapper: UiEventDetailsMapper,
    private val getEventDetails: GetEventDetails,
    private val uiEventPerformerMapper: UiEventPerformerMapper,
    private val getPerformerDetails: GetPerformerDetails,
    private val eventRepository: EventRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _state: MutableStateFlow<EventDetailsViewState> =
        MutableStateFlow(EventDetailsViewState.Loading)

    private val _performerState: MutableStateFlow<PerformerDetailsViewState> =
        MutableStateFlow(PerformerDetailsViewState.Loading)

    val state: StateFlow<EventDetailsViewState> get() = _state.asStateFlow()
    val performerState: StateFlow<PerformerDetailsViewState> get() = _performerState.asStateFlow()

    fun handleEvent(event: EventDetailsEvent) {
        when (event) {
            is EventDetailsEvent.LoadEventDetails -> subscribeToEventDetails(event.eventId)
            //is EventDetailsEvent.LoadPerformerDetails -> subscribeToEventDetails(event.performerId)
        }
    }

    private fun subscribeToEventDetails(eventId: Int) {
        viewModelScope.launch {
            try {
                val event = getEventDetails(eventId)

                onEventsDetails(event)
            } catch (t: Throwable) {
                onFailure(t)
            }
        }
    }

    private fun onEventsDetails(event: EventWithDetails) {
        val eventDetails = uiEventDetailsMapper.mapToView(event)
        _state.update { EventDetailsViewState.EventDetails(eventDetails) }
    }

    private fun onFailure(failure: Throwable) {
        _state.update {
            EventDetailsViewState.Failure
        }
    }
}