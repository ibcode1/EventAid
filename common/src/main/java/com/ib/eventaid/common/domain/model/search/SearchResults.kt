
package com.ib.eventaid.common.domain.model.search

import com.ib.eventaid.common.domain.model.event.Event

data class SearchResults(
    val event: List<Event>,
    //val venue: List<Venue>,
    //val performer: List<Performer>,
    val searchParameters: SearchParameters
)
