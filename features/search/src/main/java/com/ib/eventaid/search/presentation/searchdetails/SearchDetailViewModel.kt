package com.ib.eventaid.search.presentation.searchdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ib.eventaid.common.domain.model.event.details.EventWithDetails
import com.ib.eventaid.common.domain.usecases.GetEventDetails
import com.ib.eventaid.common.presentation.mappers.UiPerformerMapper
import com.ib.eventaid.search.presentation.searchdetails.model.mapper.UiSearchedEventDetailsMappers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchDetailViewModel @Inject constructor(
    private val uiSearchedEventDetailsMappers: UiSearchedEventDetailsMappers,
    private val getEventDetails: GetEventDetails
) : ViewModel() {

    private val _state: MutableStateFlow<SearchDetailsViewState> =
        MutableStateFlow(SearchDetailsViewState.Loading)
     val state: StateFlow<SearchDetailsViewState> get() = _state.asStateFlow()

    fun handleEvent(q:SearchDetailsEvent){
        when(q){
            is SearchDetailsEvent.LoadEventDetails-> subscribeToSearchEventDetails(q.eventId)
        }
    }

    private fun subscribeToSearchEventDetails(eventId: Int) {
        viewModelScope.launch {
            try {
                val event = getEventDetails(eventId)
                onSearchEventDetails(event)
            }catch (t:Throwable){
                onFailure(t)
            }
        }
    }

    private fun onSearchEventDetails(event:EventWithDetails) {
        val searchDetails = uiSearchedEventDetailsMappers.mapToView(event)
        _state.update { SearchDetailsViewState.EventDetails(searchDetails) }
    }

    private fun onFailure(failure: Throwable) {
        _state.update {
            SearchDetailsViewState.Failure
        }
    }


}