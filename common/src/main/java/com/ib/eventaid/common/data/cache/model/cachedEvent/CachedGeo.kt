package com.ib.eventaid.common.data.cache.model.cachedEvent

import androidx.room.Entity

@Entity(tableName = "ranges")
data class CachedGeo(
    val range:String
)
