package com.ib.eventaid.performers.presentation.performerdetails

sealed class PerformerDetailsEvent {
    data class LoadPerformerDetails(val performerId:Int):PerformerDetailsEvent()
}