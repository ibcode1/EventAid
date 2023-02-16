package com.ib.eventaid.common.data

import com.ib.eventaid.common.domain.model.event.Event
import com.ib.eventaid.common.domain.model.event.Media
import com.ib.eventaid.common.domain.model.event.details.Details
import com.ib.eventaid.common.domain.model.event.details.EventWithDetails
import com.ib.eventaid.common.domain.model.event.details.Stats
import com.ib.eventaid.common.domain.model.pagination.PaginatedEvents
import com.ib.eventaid.common.domain.model.pagination.Pagination
import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.domain.model.search.SearchParameters
import com.ib.eventaid.common.domain.model.search.SearchResults
import com.ib.eventaid.common.domain.model.taxonomy.Taxonomy
import com.ib.eventaid.common.domain.model.venue.Venue
import com.ib.eventaid.common.domain.repositories.EventRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import java.time.LocalDateTime
import javax.inject.Inject

class FakeRepository @Inject constructor() : EventRepository {

    private val venue = Venue(
        id = 93,
        name = "Madison Square Garden",
        timeZone = "America/New_York",
        address = Venue.Address(
            address1 = "",
            address2 = "",
            city = "",
            state = "",
            postalCode = "",
            country = ""
        ),
        info = Venue.Info(
            capacity = 0,
            numUpcomingEvents = 0,
            venueScore = 0.0,
            url = "",
            hasUpcomingEvents = true
        ),
        location = Venue.Location(
            lon = 0.0,
            lat = 0.0
        )
    )

    private val performer = Performer(
        id = 2090,
        name = "New York Knicks",
        awayTeam = true,
        hasUpcomingEvents = true,
        homeTeam = true,
        image = "",
        url = "",
        numUpcomingEvents = 52,
        score = 0.68,
        popularity = 0,
        slug = "",
        type = "",
    )

    private val localEventDetails = Details(
        description = "",
        stats = Stats(
            averagePrice = 73,
            highestPrice = 182,
            listingCount = 56,
            lowestPrice = 46,
            medianPrice = 0,
            visibleListingCount = 37,
            lowestPriceGoodDeals = 46,
            lowestSgBasePrice = 30,
            lowestSgBasePriceGoodDeals = 30
        ),
        taxonomy = listOf(
            Taxonomy(
                id = 1000000,
                name = "sports",
                rank = 0
            )
        ),
        venue = venue,
        performers = listOf(performer)
    )

    private val localEvent = EventWithDetails(
        id = 5776281,
        title = "Golden State Warriors at New York Knicks",
        media = Media(listOf()),
        details = localEventDetails,
        dateTimeLocal = LocalDateTime.now(),
        visibleUntilUtc = LocalDateTime.now(),
        type = "nba"
    )

    private val remoteEventDetails = Details(
        description = "",
        stats = Stats(
            averagePrice = 703,
            highestPrice = 11999,
            listingCount = 402,
            lowestPrice = 150,
            medianPrice = 480,
            visibleListingCount = 402,
            lowestPriceGoodDeals = 150,
            lowestSgBasePrice = 125,
            lowestSgBasePriceGoodDeals = 125
        ),
        taxonomy = listOf(
            Taxonomy(
                id = 1000000,
                name = "sports",
                rank = 0
            )
        ),
        venue = venue,
        performers = listOf(performer)
    )

    private val remoteEvent = EventWithDetails(
        id = 5761827,
        title = "Golden State Warriors at Brooklyn Nets",
        media = Media(listOf()),
        details = remoteEventDetails,
        dateTimeLocal = LocalDateTime.now(),
        visibleUntilUtc = LocalDateTime.now(),
        type = "nba"
    )

    val remotelySearchableEvent = SearchParameters(
        title = remoteEvent.title,
        type = remoteEvent.type,
        performer = remoteEvent.details.performers.toString()
    )

    val localEvents: List<Event> get() = mutableLocalEvents.map { it.toEvent() }
    private val mutableLocalEvents = mutableListOf(localEvent)

    val remoteEvents: List<Event> get() = mutableRemoteEvents.map { it.toEvent() }
    private val mutableRemoteEvents = mutableListOf(remoteEvent)

    override fun getEvents(): Flowable<List<Event>> {
        return Observable.just(localEvents)
            .toFlowable(BackpressureStrategy.LATEST)
    }

    override suspend fun requestMoreEvents(pageToLoad: Int, numberOfItems: Int): PaginatedEvents {
        return PaginatedEvents(
            mutableRemoteEvents,
            Pagination(page = 2, totalPages = 2)
        )
    }

    override suspend fun storeEvents(events: List<EventWithDetails>) {
        mutableLocalEvents.addAll(events)
    }

    override suspend fun getPerformerEvents(performerId: Int): Flowable<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun requestMorePerformerEvents(
        performerId: Int,
        pageToLoad: Int,
        numberOfItems: Int
    ): PaginatedEvents {
        TODO("Not yet implemented")
    }


    override suspend fun getEventTypes(): List<String> {
        return listOf("nba")
    }

    override suspend fun getEvent(eventId: Int): EventWithDetails {
        TODO("Not yet implemented")
    }

    override fun searchCachedEventsBy(searchParameters: SearchParameters): Flowable<SearchResults> {
        val (title, type, performer) = searchParameters

        val matches = mutableRemoteEvents.filter {
            it.title == title &&
                    (type.isEmpty() || it.type == type) &&
                    (performer.isEmpty() || it.details.performers.toString() == performer)
        }
            .map { it.toEvent() }

        return Observable.just(SearchResults(matches, searchParameters))
            .toFlowable(BackpressureStrategy.LATEST)
    }

    override suspend fun searchEventsRemotely(
        pageToLoad: Int,
        searchParameters: SearchParameters,
        numberOfItems: Int
    ): PaginatedEvents {
        val (title, type, performer) = searchParameters

        val matches = mutableRemoteEvents.filter {
            it.title == title && it.details.performers.toString() == performer &&
                    it.type == type }
        return PaginatedEvents(
            matches,
            Pagination(page = 1, totalPages = 1)
        )
    }

    override suspend fun storeOnboardingData(postcode: String, distance: String) {
        TODO("Not yet implemented")
    }

    override suspend fun onboardingIsComplete(): Boolean {
        return true
    }

    private fun EventWithDetails.toEvent(): Event {
        return Event(
            title, id, dateTimeLocal, visibleUntilUtc, media,
        )
    }
}