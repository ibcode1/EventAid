package com.ib.eventaid.common.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ib.eventaid.common.data.cache.model.cachedVenue.CachedVenue
import com.ib.eventaid.common.domain.model.venue.Venue

@Dao
interface VenueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(venues: List<CachedVenue>)

  @Query("SELECT * FROM venues WHERE venueId IS :venueId")
     suspend fun getVenue(venueId:Int):CachedVenue
}