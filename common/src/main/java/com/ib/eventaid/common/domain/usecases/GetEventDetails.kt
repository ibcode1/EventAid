package com.ib.eventaid.common.domain.usecases

import com.ib.eventaid.common.domain.model.event.details.EventWithDetails
import com.ib.eventaid.common.domain.repositories.EventRepository
import com.ib.eventaid.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetEventDetails @Inject constructor(
    private val eventRepository: EventRepository,
    private val dispatchersProvider: DispatchersProvider,
) {
    suspend operator fun invoke(eventId: Int): EventWithDetails {
        return withContext(dispatchersProvider.io()) {
            eventRepository.getEvent(eventId)

        }
    }
}