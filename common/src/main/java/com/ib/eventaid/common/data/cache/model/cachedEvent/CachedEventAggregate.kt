package com.ib.eventaid.common.data.cache.model.cachedEvent

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ib.eventaid.common.domain.model.event.details.EventWithDetails

data class CachedEventAggregate(
    @Embedded
    val event: CachedEventWithDetails,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId",
    )
    val images: List<CachedImage>,
    @Relation(
        entity = CachedEventPerformer::class,
        parentColumn = "eventId",
        entityColumn = "performerId",
        associateBy = Junction(
            CacheEventsWithPerformerCrossRef::class,
            parentColumn = "eventId",
            entityColumn = "performerId"
        )
    )
    val performers: List<CachedEventPerformer>,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId"
    )
    val stats: CachedStats,
) {
    companion object {
        fun fromDomain(eventWithDetails: EventWithDetails): CachedEventAggregate {
            return CachedEventAggregate(
                event = CachedEventWithDetails.fromDomain(eventWithDetails),
                images = eventWithDetails.media.images.map {
                    CachedImage.fromDomain(eventWithDetails.id, it)
                },
                performers = eventWithDetails.details.performers.map {
                    CachedEventPerformer.fromEventDomain(it, eventWithDetails.id)
                },
                stats = CachedStats.fromDomain(eventWithDetails.id, eventWithDetails.details.stats)
            )
        }
    }
}