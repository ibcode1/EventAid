package com.ib.eventaid.common.data.cache.model.cachedEvent

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["eventId","range"], indices = [Index("range")])
data class CachedEventGeoCrossRef(
    val eventId: Int,
    val range:String
)