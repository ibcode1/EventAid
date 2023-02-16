package com.ib.eventaid.common.data.cache.model.cachedEvent

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "events_performers",
    primaryKeys = ["eventId","performerId"],
    foreignKeys = [
        ForeignKey(
            entity = CachedEventWithDetails::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        ),
    ForeignKey(
        entity = CachedEventPerformer::class,
        parentColumns = ["performerId"],
        childColumns = ["performerId"],
        onDelete = ForeignKey.CASCADE
    ),
    ],
    indices = [
        Index(value = ["eventId"]),
        Index(value = ["performerId"])
    ],
)
data class CacheEventsWithPerformerCrossRef(
    val eventId:Int,
    val performerId:Int,
)