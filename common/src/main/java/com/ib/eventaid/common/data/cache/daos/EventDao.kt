package com.ib.eventaid.common.data.cache.daos

import androidx.room.*
import com.ib.eventaid.common.data.cache.model.cachedEvent.*
import io.reactivex.Flowable

@Dao
abstract class EventDao {

    @Transaction
    @Query("SELECT * FROM events ORDER BY eventId DESC")
    abstract fun getAllEvents(): Flowable<List<CachedEventAggregate>>

    @Transaction
    @Query("SELECT * FROM events WHERE eventId IS :eventId")
    abstract suspend fun getEvent(eventId: Int): CachedEventAggregate

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertEventAggregate(
        event: CachedEventWithDetails,
        image: List<CachedImage>,
        performers: List<CachedEventPerformer>,
        stats: CachedStats
    )

    suspend fun insertEventsWithDetails(eventAggregates: List<CachedEventAggregate>) {
        for (eventAggregate in eventAggregates) {
            insertEventAggregate(
                eventAggregate.event,
                eventAggregate.images,
                eventAggregate.performers,
                eventAggregate.stats
            )
        }
    }


    @Query("SELECT * FROM events")
    abstract suspend fun getPerformers(): List<CachedEventAggregate>


    @Query("SELECT DISTINCT type FROM events")
    abstract suspend fun getAllTypes(): List<String>

    @Transaction
    @Query(
        """
      SELECT * FROM events
      WHERE title LIKE '%' || :title || '%'
      AND type LIKE '%' || :type || '%'
  """
    )

    abstract fun searchEventsBy(
        title: String,
        //venue: String,
        type: String,
    ): Flowable<List<CachedEventAggregate>>
}