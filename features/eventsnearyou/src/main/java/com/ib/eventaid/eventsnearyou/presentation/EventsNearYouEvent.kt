package com.ib.eventaid.eventsnearyou.presentation

sealed class EventsNearYouEvent {
  object RequestInitialEventsList: EventsNearYouEvent()
  object RequestMoreEvents: EventsNearYouEvent()
}
