package com.ib.eventaid.common.data.cache.model.cachedTaxonomy

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ib.eventaid.common.data.cache.model.cachedEvent.CachedEventWithDetails
import com.ib.eventaid.common.domain.model.taxonomy.Taxonomy

@Entity(
    tableName = "taxonomies",
    foreignKeys = [
        ForeignKey(
            entity = CachedEventWithDetails::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index("eventId")]
)
data class CachedTaxonomy(
    @PrimaryKey(autoGenerate = true)
    val taxonomyId: Int,
    val eventId:Int,
    val name: String,
    val rank: Int
) {
    companion object {
        fun fromDomain(eventId: Int,domainModel: Taxonomy): CachedTaxonomy {
            return CachedTaxonomy(
                taxonomyId = domainModel.id,
                eventId = eventId,
                name = domainModel.name,
                rank = domainModel.rank
            )
        }
    }

    fun toDomain(): Taxonomy {
        return Taxonomy(
            taxonomyId,
            name, rank,
        )
    }
}