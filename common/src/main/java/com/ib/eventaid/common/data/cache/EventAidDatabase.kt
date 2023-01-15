package com.ib.eventaid.common.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ib.eventaid.common.data.cache.daos.EventDao
import com.ib.eventaid.common.data.cache.daos.PerformerDao
import com.ib.eventaid.common.data.cache.daos.TaxonomyDao
import com.ib.eventaid.common.data.cache.daos.VenueDao
import com.ib.eventaid.common.data.cache.model.cachedEvent.*
import com.ib.eventaid.common.data.cache.model.cachedTaxonomy.CachedTaxonomy
import com.ib.eventaid.common.data.cache.model.cachedVenue.CachedVenue

@Database(
    entities = [
        CachedImage::class,
        CachedEventWithDetails::class,
        CachedVenue::class,
        CachedEventPerformer::class,
        CachedTaxonomy::class,
        //CachedTaxonomyCrossRef::class,
        //CachedImageCrossRef::class,
        CacheEventsWithPerformerCrossRef::class,
        CachedStats::class,
    ],
    version = 4
)
@TypeConverters(Converters::class)
abstract class EventAidDatabase : RoomDatabase() {
    abstract fun venuesDao(): VenueDao
    abstract fun performerDao(): PerformerDao
    abstract fun taxonomyDao(): TaxonomyDao
    abstract fun eventDao(): EventDao
}