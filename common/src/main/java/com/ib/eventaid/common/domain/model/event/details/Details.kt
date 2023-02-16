package com.ib.eventaid.common.domain.model.event.details

import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.domain.model.taxonomy.Taxonomy
import com.ib.eventaid.common.domain.model.venue.Venue

data class Details(
    val description: String,
    val stats: Stats,
    val taxonomy: List<Taxonomy>,
    val venue: Venue,
    val performers: List<Performer>
)