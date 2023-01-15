package com.ib.eventaid.performers.domain.usecases

import com.ib.eventaid.common.domain.repositories.PerformerRepository
import javax.inject.Inject

class GetPerformers @Inject constructor(
    private val performersRepository: PerformerRepository) {
    operator fun invoke() = performersRepository.getPerformers()
        .filter { it.isNotEmpty() }
}
