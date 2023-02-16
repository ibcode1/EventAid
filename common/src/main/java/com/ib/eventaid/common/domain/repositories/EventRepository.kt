package com.ib.eventaid.common.domain.repositories

import com.ib.eventaid.common.domain.model.event.Event
import com.ib.eventaid.common.domain.model.event.details.EventWithDetails
import com.ib.eventaid.common.domain.model.pagination.PaginatedEvents
import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.domain.model.search.SearchParameters
import com.ib.eventaid.common.domain.model.search.SearchResults
import io.reactivex.Flowable

interface EventRepository {
    fun getEvents(): Flowable<List<Event>>
    suspend fun requestMoreEvents(pageToLoad: Int, numberOfItems: Int): PaginatedEvents
    suspend fun storeEvents(events: List<EventWithDetails>)

    suspend fun getPerformerEvents(performerId:Int):Flowable<List<Event>>
    suspend fun requestMorePerformerEvents(performerId: Int,pageToLoad: Int, numberOfItems: Int):PaginatedEvents

    suspend fun getEventTypes(): List<String>
    suspend fun getEvent(eventId: Int): EventWithDetails
    fun searchCachedEventsBy(searchParameters: SearchParameters): Flowable<SearchResults>


    suspend fun searchEventsRemotely(
        pageToLoad: Int,
        searchParameters: SearchParameters,
        numberOfItems: Int
    ): PaginatedEvents

    suspend fun storeOnboardingData(postcode: String, distance: String)
    suspend fun onboardingIsComplete(): Boolean
}