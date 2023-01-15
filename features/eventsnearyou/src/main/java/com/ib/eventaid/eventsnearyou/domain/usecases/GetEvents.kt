package com.ib.eventaid.eventsnearyou.domain.usecases

import com.ib.eventaid.common.domain.repositories.EventRepository
import javax.inject.Inject

class GetEvents @Inject constructor(
    private val eventsRepository: EventRepository){
    operator fun invoke() = eventsRepository.getEvents()
        .filter { it.isNotEmpty() }
}
