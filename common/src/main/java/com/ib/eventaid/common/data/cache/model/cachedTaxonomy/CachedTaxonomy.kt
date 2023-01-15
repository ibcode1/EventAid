package com.ib.eventaid.common.data.cache.model.cachedTaxonomy

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ib.eventaid.common.domain.model.taxonomy.Taxonomy

@Entity(tableName = "taxonomies")
data class CachedTaxonomy(
    @PrimaryKey(autoGenerate = false)
    val taxonomyId:Int,
    val name:String,
    val rank:Int
) {
    companion object{
        fun fromDomain(domainModel:Taxonomy):CachedTaxonomy{
            return CachedTaxonomy(
                taxonomyId = domainModel.id,
                name = domainModel.name,
                rank = domainModel.rank
            )
        }
    }
    fun toDomain():Taxonomy{
        return Taxonomy(
            taxonomyId,
            name, rank
        )
    }
}