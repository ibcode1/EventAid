package com.ib.eventaid.performers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ib.eventaid.common.domain.NetworkException
import com.ib.eventaid.common.domain.NetworkUnavailableException
import com.ib.eventaid.common.domain.NoMorePerformersException
import com.ib.eventaid.common.domain.model.pagination.Pagination
import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.presentation.UIPerformer
import com.ib.eventaid.common.presentation.mappers.UiPerformerMapper
import com.ib.eventaid.common.presentation.model.Event
import com.ib.eventaid.common.utils.createExceptionHandler
import com.ib.eventaid.performers.domain.usecases.GetPerformers
import com.ib.eventaid.performers.domain.usecases.RequestNextPageOfPerformers
import com.ib.logging.Logger
import com.ib.logging.Logger.d
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerformersFragmentViewModel @Inject constructor(
    private val getPerformers: GetPerformers,
    private val requestNextPageOfPerformers: RequestNextPageOfPerformers,
    private val uiPerformerMapper:UiPerformerMapper,
    private val compositeDisposable: CompositeDisposable
):ViewModel() {

    companion object{
        const val UI_PAGE_SIZE = Pagination.DEFAULT_PAGE_SIZE
    }

    private val _state = MutableStateFlow(PerformersViewState())
    private var currentPage = 0

    val state:StateFlow<PerformersViewState> = _state.asStateFlow()
    var isLoadingMorePerformers:Boolean = false
    var isLastPage = false

    init {
        subscribeToPerformerUpdates()
    }

    fun onEvent(performers:PerformersEvents){
        when(performers){
            is PerformersEvents.RequestMorePerformers -> loadNextPerformersPage()
        }
    }

    private fun subscribeToPerformerUpdates(){
        getPerformers()
            .doOnNext{if (hasNoPerformersStoredButCanLoadMore(it)) loadNextPerformersPage()}
            .map { performers -> performers.map {  uiPerformerMapper.mapToView(it) } }
            .filter{it.isNotEmpty()}
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {onNewPerformerList(it)},
                {onFailure(it)}
            )
            .addTo(compositeDisposable)
    }

    private fun hasNoPerformersStoredButCanLoadMore(performers: List<Performer>): Boolean {
        return performers.isEmpty()&& !state.value!!.noMorePerformers
    }

    private fun onNewPerformerList(performers: List<UIPerformer>) {
        d("Got more performers")

        val updatedPerformersSet = (state.value.performers + performers).toSet()
        _state.update { oldState ->
            oldState.copy( loading = false, performers =updatedPerformersSet.toList())
        }
    }

    private fun loadNextPerformersPage() {
        isLoadingMorePerformers = true
        val errorMessage = "Failed to fetch performers"
        val exceptionHandler = viewModelScope.createExceptionHandler(errorMessage) {onFailure(it)}

        viewModelScope.launch(exceptionHandler){
            d("Requesting More Performers")
            val pagination = requestNextPageOfPerformers(++currentPage)

            onPaginationInfoObtained(pagination)
            isLoadingMorePerformers = false
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
                    oldState.copy(noMorePerformers = true, failure = Event(failure))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}