package com.ib.eventaid.search.presentation.searchdetails

sealed class SearchDetailsEvent{
    data class LoadEventDetails(val eventId:Int):SearchDetailsEvent()
}
