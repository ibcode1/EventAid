package com.ib.eventaid.performers.presentation.performerdetails

sealed class PerformerDetailsEvent {
    data class LoadPerformerDetails(val performerId:Int):PerformerDetailsEvent()
   data class RequestInitialEventsList(val performerId:Int) : PerformerDetailsEvent()
    data class RequestMoreEvents(val performerId:Int): PerformerDetailsEvent()
}