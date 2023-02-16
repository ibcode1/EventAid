package com.ib.eventaid.search.presentation.searchdetails

import com.ib.eventaid.search.presentation.searchdetails.model.UISearchedEventDetailed


sealed class SearchDetailsViewState {
    object Loading : SearchDetailsViewState()

    data class EventDetails(
        val event: UISearchedEventDetailed
    ) : SearchDetailsViewState()

    object Failure : SearchDetailsViewState()
}