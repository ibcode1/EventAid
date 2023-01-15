package com.ib.eventaid.performers.presentation.performerdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.domain.usecases.GetPerformerDetails
import com.ib.eventaid.performers.presentation.performerdetails.model.mapper.UiPerformerDetailsMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerformerDetailsFragmentViewModel @Inject constructor(
    private val uiPerformerDetailsMapper: UiPerformerDetailsMapper,
    private val getPerformerDetails: GetPerformerDetails
):ViewModel() {
    private val _state:MutableStateFlow<PerformerDetailsViewState> = MutableStateFlow(
        PerformerDetailsViewState.Loading)

    val state:StateFlow<PerformerDetailsViewState> get() = _state.asStateFlow()

    fun handleEvent(event:PerformerDetailsEvent){
        when(event){
            is PerformerDetailsEvent.LoadPerformerDetails -> subscribeToPerformerDetails(event.performerId)
        }
    }

    private fun subscribeToPerformerDetails(performerId: Int) {
        viewModelScope.launch{
            try {
                val performer = getPerformerDetails(performerId)

                onPerformerDetails(performer)
            }catch (t:Throwable){
                onFailure(t)
            }
        }
    }

    private fun onPerformerDetails(performer: Performer) {
        val performerDetails = uiPerformerDetailsMapper.mapToView(performer)
        _state.update { PerformerDetailsViewState.PerformerDetails(performerDetails) }
    }

    private fun onFailure(failure: Throwable) {
        _state.update {
            PerformerDetailsViewState.Failure
        }
    }
}