package com.ib.eventaid.sharing.presentation

sealed class SharingEvent {
    data class GetEventToShare(val eventId: Int) : SharingEvent()
}