
package com.ib.eventaid.search.usecases

import com.ib.eventaid.common.domain.NoMoreEventsException
import com.ib.eventaid.common.domain.model.pagination.Pagination
import com.ib.eventaid.common.domain.model.pagination.Pagination.Companion.DEFAULT_PAGE_SIZE
import com.ib.eventaid.common.domain.repositories.EventRepository
import com.ib.eventaid.common.utils.DispatchersProvider
import com.ib.eventaid.common.domain.model.search.SearchParameters
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchEventsRemotely @Inject constructor(
    private val eventRepository: EventRepository,
    private val dispatchersProvider: DispatchersProvider
) {

  suspend operator fun invoke(
      pageToLoad: Int,
      searchParameters: SearchParameters,
      pageSize: Int = DEFAULT_PAGE_SIZE
  ): Pagination {
    return withContext(dispatchersProvider.io()) {
      val (events, pagination) =
          eventRepository.searchEventsRemotely(pageToLoad, searchParameters, pageSize)

      if (events.isEmpty()) {
        throw NoMoreEventsException("Couldn't find more events that match the search parameters.")
      }
      eventRepository.storeEvents(events)

      return@withContext pagination
    }
  }
}
