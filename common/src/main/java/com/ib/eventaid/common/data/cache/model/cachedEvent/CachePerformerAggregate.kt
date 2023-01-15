package com.ib.eventaid.common.data.cache.model.cachedEvent

import androidx.room.Embedded
import androidx.room.Relation
import com.ib.eventaid.common.domain.model.event.details.EventWithDetails
import com.ib.eventaid.common.domain.model.performer.Performer

data class CachePerformerAggregate(
    @Embedded
    val performer: CachedEventPerformer,
//    @Relation(
//        parentColumn = "performerId",
//        entityColumn = "performerId"
//    )
//    val event:CachedEventWithDetails

) {
    companion object{
        fun fromDomain(performer: Performer):CachePerformerAggregate{
            return CachePerformerAggregate(
                performer = CachedEventPerformer.fromDomain(performer),
                //event = CachedEventWithDetails.fromDomain(eventWithDetails)
            )
        }
    }
}