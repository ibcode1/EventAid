package com.ib.eventaid.common.data.cache.model.cachedEvent

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["eventId","performerId"], indices = [Index("performerId")])
data class CacheEventsWithPerformerCrossRef(
    val eventId:Int,
    val performerId:Int,
)
