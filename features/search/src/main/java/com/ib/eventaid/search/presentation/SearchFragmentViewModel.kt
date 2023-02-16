
package com.ib.eventaid.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ib.eventaid.common.domain.NoMoreEventsException
import com.ib.eventaid.common.domain.model.event.Event
import com.ib.eventaid.common.domain.model.pagination.Pagination
import com.ib.eventaid.common.presentation.mappers.UiEventMapper
import com.ib.eventaid.common.utils.createExceptionHandler
import com.ib.eventaid.common.domain.model.search.SearchParameters
import com.ib.eventaid.common.domain.model.search.SearchResults
import com.ib.eventaid.search.usecases.GetSearchFilters
import com.ib.eventaid.search.usecases.SearchEvents
import com.ib.eventaid.search.usecases.SearchEventsRemotely
import com.ib.logging.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
  private val uiEventMapper: UiEventMapper,
  private val searchEventsRemotely: SearchEventsRemotely,
  private val searchEvents: SearchEvents,
  private val getSearchFilters: GetSearchFilters,
  private val compositeDisposable: CompositeDisposable,
): ViewModel() {

  companion object {
    const val UI_PAGE_SIZE = Pagination.DEFAULT_PAGE_SIZE
  }

  private var currentPage = 0
  private var remoteSearchJob: Job = Job()

  private val _state = MutableStateFlow(SearchViewState())
  private val querySubject = BehaviorSubject.create<String>()
  private val typeSubject = BehaviorSubject.createDefault("")
  private val performerSubject = BehaviorSubject.createDefault("")


  val state: StateFlow<SearchViewState> = _state.asStateFlow()

  val isLastPage: Boolean
    get() = state.value.noRemoteResults

  var isLoadingMoreEvents: Boolean = false
    private set

  fun onEvent(event: SearchEvent) {
    when(event) {
      is SearchEvent.PrepareForSearch -> prepareForSearch()
      is SearchEvent.RequestMoreEvents ->loadMoreSearchEvents()
      else -> onSearchParametersUpdate(event)
    }
  }

  private fun prepareForSearch() {
    loadFilterValues()
    setupSearchSubscription()
  }

  private fun loadFilterValues() {
    val exceptionHandler = createExceptionHandler(message = "Failed to get filter values!")

    viewModelScope.launch(exceptionHandler) {
      val (types) = getSearchFilters()
      updateStateWithFilterValues(types)
    }
  }

  private fun createExceptionHandler(message: String): CoroutineExceptionHandler {
    return viewModelScope.createExceptionHandler(message) {
      onFailure(it)
    }
  }

  private fun updateStateWithFilterValues(types: List<String>) {
    _state.update { oldState ->
      oldState.updateToReadyToSearch(types)
    }
  }

  private fun setupSearchSubscription() {
    searchEvents(querySubject, typeSubject,performerSubject)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
        { onSearchResults(it) },
        { onFailure(it) }
      )
      .addTo(compositeDisposable)
  }

  private fun onSearchResults(searchResults: SearchResults) {
    val (events, searchParameters) = searchResults

    if (events.isEmpty()) {
      onEmptyCacheResults(searchParameters)
    } else {
      onEventList(events)
    }
    /*if (venue.isEmpty()){
      onEmptyCacheResults(searchParameters)
    }else{
      onVenueList(venue)
    }

    if (performer.isEmpty()){
      onEmptyCacheResults(searchParameters)
    }else{
      onPerformerList(performer)
    }*/
  }

  private fun onEmptyCacheResults(searchParameters: SearchParameters) {
    _state.update { oldState ->
      oldState.updateToSearchingRemotely()
    }
    searchRemotely(searchParameters)
  }
  private fun loadMoreSearchEvents(){
    if (state.value.searchResults.isEmpty())
      searchRemotely(SearchParameters("","",""))
  }

  private fun searchRemotely(searchParameters: SearchParameters) {
    isLoadingMoreEvents=true
    val exceptionHandler = createExceptionHandler(message = "Failed to search remotely.")

    remoteSearchJob = viewModelScope.launch(exceptionHandler) {
      Logger.d("Searching remotely...")
      val pagination = searchEventsRemotely(++currentPage, searchParameters)

      onPaginationInfoObtained(pagination)
      isLoadingMoreEvents = false
    }

    remoteSearchJob.invokeOnCompletion { it?.printStackTrace() }
  }

  private fun onSearchParametersUpdate(event: SearchEvent) {
    remoteSearchJob.cancel( // cancels the job
      CancellationException("New search parameters incoming!")
    )

    when (event) {
      is SearchEvent.QueryInput -> updateQuery(event.input)
      is SearchEvent.TypeValueSelected -> updateTypeValue(event.type)
      else -> Logger.d("Wrong SearchEvent in onSearchParametersUpdate!")
    }
  }

  private fun updateQuery(input: String) {
    resetPagination()

    querySubject.onNext(input)

    if (input.isEmpty()) {
      setNoSearchQueryState()
    } else {
      setSearchingState()
    }
  }


  private fun updateTypeValue(type: String) {
    typeSubject.onNext(type)
  }


  private fun setSearchingState() {
    _state.update { oldState -> oldState.updateToSearching() }
  }

  private fun setNoSearchQueryState() {
    _state.update { oldState -> oldState.updateToNoSearchQuery() }
  }

  private fun onEventList(events: List<Event>) {
    _state.update { oldState ->
      oldState.updateToHasSearchResults(events.map { uiEventMapper.mapToView(it) })
    }
  }

  /*private fun onVenueList(venues:List<Venue>){
    _state.update {
      oldState ->
      oldState.updateToHasVenueSearchResults(venues.map { uiVenueMapper.mapToView(it) })
    }
  }

  private fun onPerformerList(performers:List<Performer>){
    _state.update {
      oldState ->
      oldState.updateToHasPerformerSearchResults(performers.map { uiPerformerMapper.mapToView(it)})
    }
  }*/

  private fun resetPagination() {
    currentPage = 0
  }

  private fun onPaginationInfoObtained(pagination: Pagination) {
    currentPage = pagination.page
  }

  private fun onFailure(throwable: Throwable) {
    _state.update { oldState ->
      if (throwable is NoMoreEventsException) {
        oldState.updateToNoResultsAvailable()
      } else {
        oldState.updateToHasFailure(throwable)
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    compositeDisposable.clear()
  }
}
