package com.ib.eventaid.common.domain.model.performer

import com.ib.eventaid.common.domain.model.event.Event

data class PerformerDetails(
    val id:Int,
    val name:String,
    val awayTeam:Boolean,
    val hasUpcomingEvents:Boolean,
    val homeTeam:Boolean,
    val image:String,
    val url:String,
    val numUpcomingEvents:Int,
    val score:Double,
    val popularity:Int,
    val slug:String,
    val type:String,
    val event: Event
)
