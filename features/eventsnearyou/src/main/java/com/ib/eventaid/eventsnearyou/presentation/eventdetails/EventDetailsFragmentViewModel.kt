package com.ib.eventaid.eventsnearyou.presentation.eventdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ib.eventaid.common.domain.model.event.details.EventWithDetails
import com.ib.eventaid.common.domain.usecases.GetEventDetails
import com.ib.eventaid.eventsnearyou.presentation.eventdetails.model.mapper.UiEventDetailsMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsFragmentViewModel @Inject constructor(
    private val uiEventDetailsMapper: UiEventDetailsMapper,
    private val getEventDetails: GetEventDetails
) : ViewModel() {

    private val _state: MutableStateFlow<EventDetailsViewState> =
        MutableStateFlow(EventDetailsViewState.Loading)

    val state: StateFlow<EventDetailsViewState> get() = _state.asStateFlow()

    fun handleEvent(event: EventDetailsEvent) {
        when (event) {
            is EventDetailsEvent.LoadEventDetails -> subscribeToEventDetails(event.eventId)
        }
    }

    private fun subscribeToEventDetails(eventId: Int) {
        viewModelScope.launch {
            delay(2000)
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
        _state.update { EventDetailsViewState.EventDetails(eventDetails)}
    }
    private fun onFailure(failure: Throwable) {
        _state.update {
            EventDetailsViewState.Failure
        }
    }
}