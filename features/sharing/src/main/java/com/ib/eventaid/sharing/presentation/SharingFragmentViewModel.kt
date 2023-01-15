package com.ib.eventaid.sharing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ib.eventaid.common.domain.usecases.GetEventDetails
import com.ib.eventaid.sharing.presentation.model.mappers.UiEventToShareMappers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharingFragmentViewModel @Inject constructor(
    private val getEventDetails:GetEventDetails,
    private val uiEventToShareMappers: UiEventToShareMappers
):ViewModel() {

    val viewState:StateFlow<SharingViewState> get() = _viewState

    private val _viewState = MutableStateFlow(SharingViewState())

    fun onEvent(event: SharingEvent){
        when(event){
            is SharingEvent.GetEventToShare->getEventToShare(event.eventId)
        }
    }

    private fun getEventToShare(eventId: Int) {
        viewModelScope.launch {
            val event = getEventDetails(eventId)

            _viewState.update { oldState ->
                oldState.copy(eventToShare = uiEventToShareMappers.mapToView(event))
            }
        }
    }
}