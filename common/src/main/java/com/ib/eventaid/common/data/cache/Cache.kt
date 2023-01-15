package com.ib.eventaid.common.data.cache

import com.ib.eventaid.common.data.cache.model.cachedEvent.CachePerformerAggregate
import com.ib.eventaid.common.data.cache.model.cachedEvent.CachedEventAggregate
import com.ib.eventaid.common.data.cache.model.cachedEvent.CachedEventPerformer
import com.ib.eventaid.common.data.cache.model.cachedTaxonomy.CachedTaxonomy
import com.ib.eventaid.common.data.cache.model.cachedVenue.CachedVenue
import io.reactivex.Flowable

interface Cache {
    suspend fun storeVenues(venue: List<CachedVenue>)
    suspend fun getVenue(venueId:Int):CachedVenue


    suspend fun storeTaxonomies(taxonomy: List<CachedTaxonomy>)
    suspend fun getTaxonomy(taxonomyId: List<Int>):CachedTaxonomy

    //in performer
     fun getPerformers():Flowable<List<CachePerformerAggregate>>

     //for performer module
    suspend fun storePerformer(performer: List<CachedEventPerformer>)
    suspend fun getPerformer(performerId: Int):CachedEventPerformer

    //suspend fun getEventPerformers():Flowable<List<CachedEventPerformer>>

    fun getNearbyEvents(): Flowable<List<CachedEventAggregate>>
    suspend fun storeNearbyEvents(events: List<CachedEventAggregate>)
    suspend fun getEvent(eventId:Int):CachedEventAggregate


    suspend fun getAllTypes(): List<String>

    //suspend fun getAllVenues(): List<Venue>
    //suspend fun getAllPerformers(): List<Performer>
    fun searchEventsBy(
        title: String,
        //venue: String,
        type: String,
        performer: String
    ): Flowable<List<CachedEventAggregate>>
}
