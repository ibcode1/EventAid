package com.ib.eventaid.eventsnearyou.presentation.eventdetails.model

import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.presentation.UIPerformer
import com.ib.eventaid.eventsnearyou.presentation.eventdetails.EventDetailsViewState
import java.time.LocalDateTime

data class UIEventDetailed(
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
    val performer: List<Performer>,
    val date: LocalDateTime,
    val type: List<String>,
    val city: String,
    val price:String,
    val venue: String,
    val capacity: Int,
    val score: Double,
    val address: String,
    val taxonomy: List<String>
)