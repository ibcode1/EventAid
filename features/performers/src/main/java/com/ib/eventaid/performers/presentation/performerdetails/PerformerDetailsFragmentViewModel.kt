package com.ib.eventaid.performers.presentation.performerdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import com.ib.eventaid.common.data.api.ApiConstants
import com.ib.eventaid.common.data.api.PerformerAidApi
import com.ib.eventaid.common.data.api.model.mappers.ApiEventMapper
import com.ib.eventaid.common.data.api.model.mappers.ApiPaginationMapper
import com.ib.eventaid.common.domain.NetworkException
import com.ib.eventaid.common.domain.NetworkUnavailableException
import com.ib.eventaid.common.domain.NoMorePerformersException
import com.ib.eventaid.common.domain.model.pagination.PaginatedEvents
import com.ib.eventaid.common.domain.model.pagination.Pagination
import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.domain.repositories.EventRepository
import com.ib.eventaid.common.domain.usecases.GetEvents
import com.ib.eventaid.common.domain.usecases.GetPerformerDetails
import com.ib.eventaid.common.domain.usecases.GetPerformerEvents
import com.ib.eventaid.common.presentation.UIPerformerEvents
import com.ib.eventaid.common.presentation.mappers.UiEventMapper
import com.ib.eventaid.common.presentation.mappers.UiPerformerEventsMapper
import com.ib.eventaid.common.presentation.model.Event
import com.ib.eventaid.common.utils.DispatchersProvider
import com.ib.eventaid.common.utils.createExceptionHandler
import com.ib.eventaid.performers.domain.usecases.RequestNextPageOfEvents
import com.ib.eventaid.performers.presentation.performerdetails.model.mapper.UiPerformerDetailsMapper
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
class PerformerDetailsFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val uiPerformerDetailsMapper: UiPerformerDetailsMapper,
    private val getPerformerDetails: GetPerformerDetails,
    private val api: PerformerAidApi,
    private val requestNextPageOfEvents: RequestNextPageOfEvents,
    private val eventRepository: EventRepository,
    private val uiEventMapper: UiEventMapper,
    private val uiPerformerEventsMapper: UiPerformerEventsMapper,
    private val apiEventMapper: ApiEventMapper,
    private val apiPaginationMapper: ApiPaginationMapper,
    private val dispatchersProvider: DispatchersProvider,
    private val getPerformerEvents: GetPerformerEvents,
    private val getEvents: GetEvents,
    private val compositeDisposable: CompositeDisposable

) : ViewModel() {

    private val client = ApiConstants.CLIENT
    private var currentPage = 0

    companion object {
        const val UI_PAGE_SIZE = Pagination.DEFAULT_PAGE_SIZE
        const val PERFORMER_ID = "id"
    }

    private var performerId: Int? = null


    var isLastPage = false

    var isLoadingMoreEvents: Boolean = false
        private set


    private val _perfState: MutableStateFlow<PerformerDetailsViewState> =
        MutableStateFlow(PerformerDetailsViewState.Loading)
    val perfState: StateFlow<PerformerDetailsViewState> get() = _perfState.asStateFlow()

    private val _state = MutableStateFlow(PerformerEventsViewState())
    val state: StateFlow<PerformerEventsViewState> = _state.asStateFlow()


    init {
        performerId = savedStateHandle.get<Int>(PERFORMER_ID)
        subscribeToPerformerDetails(performerId!!)
    }


    fun handleEvent(event: PerformerDetailsEvent) {
        when (event) {
            is PerformerDetailsEvent.LoadPerformerDetails -> subscribeToPerformerDetails(event.performerId)
            is PerformerDetailsEvent.RequestInitialEventsList -> loadPerformerEvent(event.performerId)
            is PerformerDetailsEvent.RequestMoreEvents -> loadNextPerformerEventPage(event.performerId)
        }
    }

    private fun subscribeToPerformerDetails(performerId: Int) {
        viewModelScope.launch {
            try {
                val performer = getPerformerDetails(performerId)
                val event = loadPerformerEvent(performerId)
                //requestPerformerEvents(performerId, 1, UI_PAGE_SIZE)

                getPerformerEvents(performerId)
                    .map { events ->
                        events.map {
                            uiPerformerEventsMapper.mapToView(it) } }
                    .observeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { onNewEventList(it) },
                        { onFailure(it) },
                        {event}
                    ).addTo(compositeDisposable)
                onPerformerDetails(performer)

            } catch (t: Throwable) {
                onFailure(t)
            }
        }
    }

    private fun onPerformerDetails(performer: Performer) {
        val performerDetails = uiPerformerDetailsMapper.mapToView(performer)
        _perfState.update {
            PerformerDetailsViewState.PerformerDetails(
                performerDetails
            )
        }
    }

//    private suspend fun requestPerformerEvents(
//        performerId: Int,
//        pageToLoad: Int,
//        numberOfItems: Int
//    ): PaginatedEvents {
//
//        try {
//            val (apiEvents, apiPagination) = api.getEvents(
//                performerId,
//                pageToLoad,
//                numberOfItems,
//                client)
//            return PaginatedEvents(
//                apiEvents?.map {
//                    apiEventMapper.mapToDomain(it)
//                }.orEmpty(),
//                apiPaginationMapper.mapToDomain(apiPagination))
//
//        } catch (exception: HttpException) {
//            throw NetworkException(exception.message ?: "code ${exception.statusCode}")
//        }
//    }

    private fun onNewEventList(event: List<UIPerformerEvents>) {
        Logger.d("more Event")
        val updatedEventSet = (state.value.uiPerformerEvents + event).toSet()
        _state.update { oldState ->
            oldState.copy(
                loading = false,
                uiPerformerEvents = updatedEventSet.toList(),
                noMoreEvents = false,
                failure = null,
            )
        }
    }

    private fun loadPerformerEvent(performerId: Int) {
        if (state.value.uiPerformerEvents.isEmpty())
            loadNextPerformerEventPage(performerId)
    }

    private fun loadNextPerformerEventPage(performerId: Int) {
        isLoadingMoreEvents = true
        val errorMessage = "Failed to fetch more of the Performer events"
        val exceptionHandler = viewModelScope.createExceptionHandler(errorMessage) { onFailure(it) }

        viewModelScope.launch(exceptionHandler) {
            Logger.d("Requesting More PerformerEvent")
            val pagination = requestNextPageOfEvents(performerId,++currentPage)

            onPaginationInfoObtained(pagination)
            isLoadingMoreEvents = false
        }
    }

    private fun onPaginationInfoObtained(pagination: Pagination) {
        currentPage = pagination.page
        isLastPage = !pagination.canLoadMore
    }


    private fun onFailure(failure: Throwable) {
        when (failure) {
            is NetworkException,
            is NetworkUnavailableException -> {
                _state.update { oldState ->
                    oldState.copy(loading = false, failure = Event(failure))
                }
            }
            is NoMorePerformersException -> {
                _state.update { oldState ->
                    oldState.copy(noMoreEvents = true, failure = Event(failure))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}
