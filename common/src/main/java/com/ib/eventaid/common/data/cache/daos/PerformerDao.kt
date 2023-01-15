package com.ib.eventaid.common.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ib.eventaid.common.data.cache.model.cachedEvent.CachePerformerAggregate
import com.ib.eventaid.common.data.cache.model.cachedEvent.CachedEventPerformer
import io.reactivex.Flowable

@Dao
interface PerformerDao {

    @Transaction
    @Query("SELECT * FROM performers ORDER BY performerId DESC")
    fun getAllPerformers():Flowable<List<CachePerformerAggregate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(performers: List<CachedEventPerformer>)

    @Transaction
    @Query("SELECT * FROM performers WHERE performerId IS :performerId")
    suspend fun getPerformer(performerId:Int):CachedEventPerformer


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerformerAggregate(
        performer: CachedEventPerformer
    )

    suspend fun insertPerformer(performerAggregates: List<CachePerformerAggregate>){
        for (performerAggregate in performerAggregates){
            insertPerformerAggregate(
                performerAggregate.performer
            )
        }
    }
}