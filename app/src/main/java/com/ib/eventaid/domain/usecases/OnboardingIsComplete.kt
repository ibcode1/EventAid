
package com.ib.eventaid.domain.usecases

import com.ib.eventaid.common.domain.repositories.EventRepository
import javax.inject.Inject

class OnboardingIsComplete @Inject constructor(
    private val repository: EventRepository
) {
  suspend operator fun invoke() = repository.onboardingIsComplete()
}
