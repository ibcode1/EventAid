package com.ib.eventaid.eventsnearyou.domain.usecases

import com.ib.eventaid.common.domain.NoMoreEventsException
import com.ib.eventaid.common.domain.model.pagination.Pagination
import com.ib.eventaid.common.domain.repositories.EventRepository
import com.ib.eventaid.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestNextPageOfEvents @Inject constructor(
    private val eventRepository: EventRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(
        pageToLoad: Int,
        pageSize: Int = Pagination.DEFAULT_PAGE_SIZE
    ): Pagination {
      return withContext(dispatchersProvider.io()) {
        val (events, pagination) = eventRepository.requestMoreEvents(pageToLoad, pageSize)

        if (events.isEmpty()) {
          throw NoMoreEventsException("No events nearby :(")
        }
        eventRepository.storeEvents(events)
        return@withContext pagination
      }
    }
}
