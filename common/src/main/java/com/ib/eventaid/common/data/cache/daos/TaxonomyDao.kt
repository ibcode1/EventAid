package com.ib.eventaid.common.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ib.eventaid.common.data.cache.model.cachedTaxonomy.CachedTaxonomy

@Dao
interface TaxonomyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taxonomies: List<CachedTaxonomy>)

    @Query("SELECT * FROM taxonomies WHERE :taxonomyId = taxonomyId")
    suspend fun getTaxonomy(taxonomyId: List<Int>): CachedTaxonomy
}
