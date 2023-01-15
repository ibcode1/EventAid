package com.ib.eventaid.eventsnearyou.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ib.eventaid.common.domain.NetworkException
import com.ib.eventaid.common.domain.NetworkUnavailableException
import com.ib.eventaid.common.domain.NoMoreEventsException
import com.ib.eventaid.common.domain.model.pagination.Pagination
import com.ib.eventaid.common.presentation.UIEvent
import com.ib.eventaid.common.presentation.mappers.UiEventMapper
import com.ib.eventaid.common.presentation.model.Event
import com.ib.eventaid.common.utils.createExceptionHandler
import com.ib.eventaid.eventsnearyou.domain.usecases.GetEvents
import com.ib.eventaid.eventsnearyou.domain.usecases.RequestNextPageOfEvents
import com.ib.logging.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsNearYouFragmentViewModel @Inject constructor(
  private val getEvents: GetEvents,
  private val requestNextPageOfEvents: RequestNextPageOfEvents,
  private val uiEventMapper: UiEventMapper,
  private val compositeDisposable: CompositeDisposable
): ViewModel() {

  companion object {
    const val UI_PAGE_SIZE = Pagination.DEFAULT_PAGE_SIZE
  }

  init {
    subscribeToEventUpdates()
  }

  private val _state = MutableStateFlow(EventsNearYouViewState())
  private var currentPage = 0

  val state: StateFlow<EventsNearYouViewState> = _state.asStateFlow()

  val isLastPage: Boolean
    get() = state.value.noMoreEventsNearby

  var isLoadingMoreEvents: Boolean = false
    private set

  fun onEvent(event: EventsNearYouEvent) {
    when(event) {
      is EventsNearYouEvent.RequestInitialEventsList -> loadEvents()
      is EventsNearYouEvent.RequestMoreEvents -> loadNextEventPage()
    }
  }

  private fun subscribeToEventUpdates() {
    getEvents()
      .map { events -> events.map { uiEventMapper.mapToView(it) } }
      .subscribeOn(Schedulers.computation())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
        { onNewEventList(it) },
        { onFailure(it) }
      )
      .addTo(compositeDisposable)
  }

  private fun onNewEventList(events: List<UIEvent>) {
    Logger.d("Got more events!")

    val updatedEventSet = (state.value.events + events).toSet()

    _state.update { oldState ->
      oldState.copy(loading = false, events = updatedEventSet.toList())
    }
  }

  private fun loadEvents() {
    if (state.value.events.isEmpty()) {
      loadNextEventPage()
    }
  }

  private fun loadNextEventPage() {
    isLoadingMoreEvents = true
    val errorMessage = "Failed to fetch nearby events"
    val exceptionHandler = viewModelScope.createExceptionHandler(errorMessage) { onFailure(it) }

    viewModelScope.launch(exceptionHandler) {
      Logger.d("Requesting more events.")
      val pagination = requestNextPageOfEvents(++currentPage)

      onPaginationInfoObtained(pagination)
      isLoadingMoreEvents = false
    }
  }

  private fun onPaginationInfoObtained(pagination: Pagination) {
    currentPage = pagination.page
  }

  private fun onFailure(failure: Throwable) {
    when (failure) {
      is NetworkException,
      is NetworkUnavailableException -> {
        _state.update { oldState ->
          oldState.copy(loading = false, failure = Event(failure))
        }
      }
      is NoMoreEventsException -> {
        _state.update { oldState ->
          oldState.copy(noMoreEventsNearby = true, failure = Event(failure))
        }
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    compositeDisposable.clear()
  }
}
