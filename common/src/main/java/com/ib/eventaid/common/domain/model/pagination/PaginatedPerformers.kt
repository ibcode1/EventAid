package com.ib.eventaid.common.domain.model.pagination

import com.ib.eventaid.common.domain.model.event.details.EventWithDetails
import com.ib.eventaid.common.domain.model.performer.Performer

data class PaginatedPerformers(
    val performers:List<Performer>,
    val pagination:Pagination
)