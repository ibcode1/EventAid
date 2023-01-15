package com.ib.eventaid.search.presentation

import com.ib.eventaid.common.presentation.UIEvent
import com.ib.eventaid.common.presentation.UIPerformer
import com.ib.eventaid.common.presentation.model.Event
import com.ib.eventaid.common.presentation.UIVenue

data class SearchViewState(
    val noSearchQuery: Boolean = true,
    val searchResults: List<UIEvent> = emptyList(),
    val typeFilterValues: Event<List<String>> = Event(emptyList()),
    val venueSearchResults: List<UIVenue> = emptyList(),
    val performerSearchResults: List<UIPerformer> = emptyList(),
    val searchingRemotely: Boolean = false,
    val noRemoteResults: Boolean = false,
    val failure: Event<Throwable>? = null
) {
    fun updateToReadyToSearch(types: List<String>): SearchViewState {
        return copy(
            typeFilterValues = Event(types)
        )
    }

    fun updateToNoSearchQuery(): SearchViewState {
        return copy(
            noSearchQuery = true,
            searchResults = emptyList(),
            noRemoteResults = false
        )
    }

    fun updateToSearching(): SearchViewState {
        return copy(
            noSearchQuery = false,
            searchingRemotely = false,
            noRemoteResults = false
        )
    }

    fun updateToSearchingRemotely(): SearchViewState {
        return copy(
            searchingRemotely = true,
            searchResults = emptyList()
        )
    }

    fun updateToHasSearchResults(events: List<UIEvent>): SearchViewState {
        return copy(
            noSearchQuery = false,
            searchResults = events,
            searchingRemotely = false,
            noRemoteResults = false
        )
    }

    fun updateToHasVenueSearchResults(venues: List<UIVenue>): SearchViewState {
        return copy(
            noSearchQuery = false,
            venueSearchResults = venues,
            searchingRemotely = false,
            noRemoteResults = false
        )
    }

    fun updateToHasPerformerSearchResults(performer: List<UIPerformer>): SearchViewState {
        return copy(
            noSearchQuery = false,
            performerSearchResults = performer,
            searchingRemotely = false,
            noRemoteResults = false
        )
    }

    fun updateToNoResultsAvailable(): SearchViewState {
        return copy(searchingRemotely = false, noRemoteResults = true)
    }

    fun updateToHasFailure(throwable: Throwable): SearchViewState {
        return copy(failure = Event(throwable))
    }
}
