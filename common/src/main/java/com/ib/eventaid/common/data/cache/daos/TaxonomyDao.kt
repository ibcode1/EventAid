package com.ib.eventaid.common.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ib.eventaid.common.data.cache.model.cachedEvent.CachedEventAggregate
import com.ib.eventaid.common.data.cache.model.cachedTaxonomy.CachedTaxonomy
import io.reactivex.Flowable

@Dao
interface TaxonomyDao {

    @Transaction
    @Query("SELECT * FROM taxonomies ORDER BY taxonomyId DESC")
    abstract fun getAllTaxonomies(): Flowable<List<CachedTaxonomy>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taxonomies: List<CachedTaxonomy>)

    @Transaction
    @Query("SELECT * FROM taxonomies WHERE :taxonomyId = taxonomyId")
    suspend fun getTaxonomy(taxonomyId: List<Int>): CachedTaxonomy
}
