package com.ib.eventaid.search.usecases

import com.ib.eventaid.common.domain.repositories.EventRepository
import com.ib.eventaid.common.utils.DispatchersProvider
import com.ib.eventaid.common.domain.model.search.SearchFilters
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSearchFilters @Inject constructor(
    private val eventRepository: EventRepository,
    private val dispatchersProvider: DispatchersProvider
) {

    companion object {
        const val NO_FILTER_SELECTED = "Any"
    }

    suspend operator fun invoke(): SearchFilters {
        return withContext(dispatchersProvider.io()) {

            val types = listOf(NO_FILTER_SELECTED) + eventRepository.getEventTypes()

            return@withContext SearchFilters(types)
        }
    }
}
