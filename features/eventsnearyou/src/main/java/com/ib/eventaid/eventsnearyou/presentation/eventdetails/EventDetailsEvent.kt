package com.ib.eventaid.eventsnearyou.presentation.eventdetails

sealed class EventDetailsEvent {
    data class LoadEventDetails(val eventId:Int):EventDetailsEvent()}