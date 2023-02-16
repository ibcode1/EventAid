package com.ib.eventaid.common.domain.usecases

import com.ib.eventaid.common.domain.repositories.EventRepository
import javax.inject.Inject

class GetPerformerEvents @Inject constructor(
    private val eventRepository: EventRepository,
) {

     suspend operator fun invoke(performerId: Int) =
        eventRepository.getPerformerEvents(performerId)
            .filter{it.isNotEmpty()}
    }

