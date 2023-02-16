package com.ib.eventaid.common.domain.model.pagination
import com.ib.eventaid.common.domain.model.event.details.EventWithDetails

data class PaginatedEvents(
    val events:List<EventWithDetails>,
    val pagination:Pagination
)