package com.ib.eventaid.common.domain.model.event.details

import com.ib.eventaid.common.domain.model.event.Media
import java.time.LocalDateTime

data class EventWithDetails(
    val id:Int,
    val title:String,
    val media:Media,
    //val image:List<String>,
    val details: Details,
    val dateTimeLocal:LocalDateTime,
    val visibleUntilUtc: LocalDateTime,
    val type:String
)