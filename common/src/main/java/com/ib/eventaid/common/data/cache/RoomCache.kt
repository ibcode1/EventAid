package com.ib.eventaid.common.data.cache

import com.ib.eventaid.common.data.cache.daos.*
import com.ib.eventaid.common.data.cache.model.cachedEvent.CacheEventsWithPerformerCrossRef
import com.ib.eventaid.common.data.cache.model.cachedEvent.CachePerformerAggregate
import com.ib.eventaid.common.data.cache.model.cachedEvent.CachedEventAggregate
import com.ib.eventaid.common.data.cache.model.cachedEvent.CachedEventPerformer
import com.ib.eventaid.common.data.cache.model.cachedTaxonomy.CachedTaxonomy
import com.ib.eventaid.common.data.cache.model.cachedVenue.CachedVenue
import io.reactivex.Flowable
import javax.inject.Inject

class RoomCache @Inject constructor(
    private val eventDao: EventDao,
    private val taxonomyDao: TaxonomyDao,
    private val venueDao: VenueDao,
    private val performerDao: PerformerDao,
) : Cache {
    override suspend fun storeVenues(venue: List<CachedVenue>) {
        venueDao.insert(venue)
    }

    override suspend fun getVenue(venueId: Int): CachedVenue {
        return venueDao.getVenue(venueId)
    }

    override suspend fun storePerformer(performer: List<CachedEventPerformer>) {
        performerDao.insert(performer)
    }

    override suspend fun getPerformer(performerId: Int): CachedEventPerformer {
        return performerDao.getPerformer(performerId)
    }

    override suspend fun storeTaxonomies(taxonomy: List<CachedTaxonomy>) {
        taxonomyDao.insert(taxonomy)
    }

    override suspend fun getTaxonomy(taxonomyId: List<Int>): CachedTaxonomy {
        return taxonomyDao.getTaxonomy(taxonomyId)
    }

    override fun getPerformers(): Flowable<List<CachePerformerAggregate>> {
        return performerDao.getAllPerformers()
    }

    override fun getNearbyEvents(): Flowable<List<CachedEventAggregate>> {
        return eventDao.getAllEvents()
    }

    override suspend fun storeNearbyEvents(events: List<CachedEventAggregate>) {
        eventDao.insertEventsWithDetails(events)
    }

    override suspend fun getEvent(eventId: Int): CachedEventAggregate {
        return eventDao.getEvent(eventId)
    }


    override fun getPerformerEvents(performerId: Int): Flowable<List<CachedEventAggregate>> {
        return eventDao.getPerformerEvents(performerId)
    }

    override suspend fun getAllTypes(): List<String> {
        return eventDao.getAllTypes()
    }

    override fun searchEventsBy(
        title: String,
        //venue: String,
        type: String,
        performer: String
    ): Flowable<List<CachedEventAggregate>> {
        return eventDao.searchEventsBy(title,type)
    }

//    override suspend fun getAllVenues(): List<Venue> {
//        return venueDao.getAllVenues()
//    }

}