package com.ib.eventaid.common.domain.model.event

import com.ib.eventaid.common.domain.model.performer.Performer
import java.time.LocalDateTime

data class Event(
    val title:String,
    val id:Int,
    val dateTimeLocal: LocalDateTime,
    val visibleUntilUtc:LocalDateTime,
    val media: Media,
    //val performer:Int
    //val performer: List<Performer>
    //val image: List<String>,
)