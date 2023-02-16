package com.ib.eventaid.newnav.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ib.eventaid.common.utils.DispatchersProvider
import com.ib.eventaid.common.utils.createExceptionHandler
import com.ib.eventaid.newnav.R
import com.ib.eventaid.newnav.domain.usecases.StoreNavData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewNavFragmentViewModel @Inject constructor(
    private val storeNavData: StoreNavData,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    companion object {
        private const val MAX_POSTCODE_LENGTH = 5
    }

    val viewState: StateFlow<NewNavViewState> get() = _viewState
    val viewEffects: SharedFlow<NewNavViewEffect> get() = _viewEffects

    private val _viewState = MutableStateFlow(NewNavViewState())
    private val _viewEffects = MutableSharedFlow<NewNavViewEffect>()

    fun onEvent(event: NewNavEvent) {
        when (event) {
            is NewNavEvent.PostcodeChanged -> validateNewPostcodeValue(event.newPostcode)
            is NewNavEvent.DistanceChanged -> validateNewDistanceValue(event.newDistance)
            is NewNavEvent.SubmitButtonClicked -> wrapUpNav()
        }
    }

    private fun validateNewPostcodeValue(newPostcode: String) {
        val validPostcode = newPostcode.length == MAX_POSTCODE_LENGTH
        val postcodeError = if (validPostcode || newPostcode.isEmpty()) {
            R.string.no_error
        } else {
            R.string.postcode_error
        }
        _viewState.value = viewState.value.copy(
            postcode = newPostcode,
            postcodeError = postcodeError
        )
    }

    private fun validateNewDistanceValue(newDistance: String) {
        val distanceError = when {
            newDistance.isNotEmpty() && newDistance > "500mi" -> {
                R.string.distance_error
            }
            newDistance == "0mi" -> {
                R.string.distance_error_cannot_be_zero
            }
            else -> {
                R.string.no_error
            }
        }

        _viewState.value = viewState.value.copy(
            distance = newDistance,
            distanceError = distanceError
        )
    }

    private fun wrapUpNav() {
        val errorMessage = "Failed to store onboarding data"
        val exceptionHandler = viewModelScope.createExceptionHandler(errorMessage) { onFailure(it) }
        val (postcode, distance) = viewState.value

        viewModelScope.launch(exceptionHandler) {
            withContext(dispatchersProvider.io()) { storeNavData(postcode, distance) }
            _viewEffects.emit(NewNavViewEffect.NavigateToEventsNearYou)
        }
    }

    private fun onFailure(throwable: Throwable) {
        // TODO: Handle failures
    }
}