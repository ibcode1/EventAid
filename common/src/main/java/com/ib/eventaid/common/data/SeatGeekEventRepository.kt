package com.ib.eventaid.common.data

import com.ib.eventaid.common.data.api.ApiConstants
import com.ib.eventaid.common.data.api.EventAidApi
import com.ib.eventaid.common.data.api.model.mappers.ApiEventMapper
import com.ib.eventaid.common.data.api.model.mappers.ApiPaginationMapper
import com.ib.eventaid.common.data.cache.Cache
import com.ib.eventaid.common.data.cache.model.cachedEvent.CachedEventAggregate
import com.ib.eventaid.common.data.cache.model.cachedEvent.CachedEventPerformer
import com.ib.eventaid.common.data.cache.model.cachedVenue.CachedVenue
import com.ib.eventaid.common.data.preferences.Preferences
import com.ib.eventaid.common.domain.NetworkException
import com.ib.eventaid.common.domain.model.event.Event
import com.ib.eventaid.common.domain.model.event.details.EventWithDetails
import com.ib.eventaid.common.domain.model.pagination.PaginatedEvents
import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.domain.model.search.SearchParameters
import com.ib.eventaid.common.domain.model.search.SearchResults
import com.ib.eventaid.common.domain.repositories.EventRepository
import io.reactivex.Flowable
import retrofit2.HttpException
import javax.inject.Inject


class SeatGeekEventRepository @Inject constructor(
    private val api: EventAidApi,
    private val cache: Cache,
    private val apiEventMapper: ApiEventMapper,
    private val apiPaginationMapper: ApiPaginationMapper,
    private val preferences: Preferences,
) : EventRepository {
    private val client = ApiConstants.CLIENT

    override fun getEvents(): Flowable<List<Event>> {
        return cache.getNearbyEvents()
            .distinctUntilChanged()
            .map { eventList ->
                eventList.map { it.event.toEventDomain(it.images, it.performers) }
            }
    }

    override suspend fun requestMoreEvents(pageToLoad: Int, numberOfItems: Int): PaginatedEvents {

        val postalCode = preferences.getPostcode()
        val maxRangeMiles = preferences.getMaxDistanceAllowedToGetEvents()

        try {
            val (apiEvents, apiPagination) = api.getNearbyEvents(
                pageToLoad,
                numberOfItems,
                postalCode,
                maxRangeMiles,
                client
            )
            return PaginatedEvents(
                apiEvents?.map {
                    apiEventMapper.mapToDomain(it)
                }.orEmpty(),
                apiPaginationMapper.mapToDomain(apiPagination)
            )
        } catch (exception: HttpException) {
            throw NetworkException(exception.message ?: "Code ${exception.code()}")
        }
    }

    override suspend fun storeEvents(events: List<EventWithDetails>) {
        val venue = events.map { CachedVenue.fromDomain(it.details.venue) }
        cache.storeVenues(venue)
        cache.storeNearbyEvents(events.map { CachedEventAggregate.fromDomain(it) })
    }

    override suspend fun getEventTypes(): List<String> {
        return cache.getAllTypes()
    }

    override suspend fun getEvent(eventId: Int): EventWithDetails {
        val (event, image, performers, stats) = cache.getEvent(eventId)
        val venue = cache.getVenue(event.venueId)

        return event.toDomain(
            venue,
            performers,
            stats,
            image
        )
    }

    override fun searchCachedEventsBy(searchParameters: SearchParameters): Flowable<SearchResults> {
        val (title, type, performer) = searchParameters

        return cache.searchEventsBy(title, type, performer)
            .distinctUntilChanged()
            .map { eventList ->
                eventList.map {
                    it.event.toEventDomain(
                        it.images, it.performers
                    )
                }
            }
            .map { SearchResults(it, searchParameters) }
    }

    override suspend fun searchEventsRemotely(
        pageToLoad: Int,
        searchParameters: SearchParameters,
        numberOfItems: Int
    ): PaginatedEvents {

        val maxRangeMiles = preferences.getMaxDistanceAllowedToGetEvents()
        //val postalCode = preferences.getPostcode()

        val (apiEvents, apiPagination) = api.searchEventsBy(
            searchParameters.title,
            //searchParameters.venue,
            searchParameters.type,
            //searchParameters.performer,
            pageToLoad,
            numberOfItems,
            //postalCode,
            maxRangeMiles,
            client
        )

        return PaginatedEvents(
            apiEvents?.map {
                apiEventMapper.mapToDomain(it)
            }.orEmpty(),
            apiPaginationMapper.mapToDomain(apiPagination)
        )
    }

    override suspend fun storeOnboardingData(postcode: String, distance: String) {
        with(preferences) {
            putPostcode(postcode)
            putMaxDistanceAllowedToGetEvents(distance)
        }
    }

    override suspend fun onboardingIsComplete(): Boolean {
        return preferences.getPostcode().isNotEmpty() &&
                preferences.getMaxDistanceAllowedToGetEvents() > "0mi"
    }


    /*override suspend fun getEventPerformers(): List<Performer> {
        return cache.getAllPerformers()
    }*/
}