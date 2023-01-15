package com.ib.eventaid.search.usecases

import com.ib.eventaid.common.domain.repositories.EventRepository
import com.ib.eventaid.common.domain.model.search.SearchParameters
import com.ib.eventaid.common.domain.model.search.SearchResults
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import io.reactivex.functions.Function3
import javax.inject.Inject

class SearchEvents @Inject constructor(private val eventRepository: EventRepository) {

    private val combiningFunction: Function3<String, String, String, SearchParameters>
        get() = Function3 { query, type, performer ->
            SearchParameters(query, type, performer)
        }

    operator fun invoke(
        querySubject: BehaviorSubject<String>,
        typeSubject: BehaviorSubject<String>,
        performerSubject: BehaviorSubject<String>,
    ): Flowable<SearchResults> {
        val query = querySubject
            .debounce(500L, TimeUnit.MILLISECONDS)
            .map { it.trim() }
            .filter { it.length >= 2 }

        val type = typeSubject.replaceUIEmptyValue()
        val performer = performerSubject.replaceUIEmptyValue()

        return Observable.combineLatest(query, type, performer, combiningFunction)
            .toFlowable(BackpressureStrategy.LATEST)
            .switchMap { parameters: SearchParameters ->
                eventRepository.searchCachedEventsBy(parameters)
            }
    }

    private fun BehaviorSubject<String>.replaceUIEmptyValue() = map {
        if (it == GetSearchFilters.NO_FILTER_SELECTED) "" else it
    }
}
