package com.ib.eventaid.onboarding.domain.usecases

import com.ib.eventaid.common.domain.repositories.EventRepository
import javax.inject.Inject

class StoreOnboardingData @Inject constructor(
    private val repository: EventRepository
)
{
    suspend operator fun invoke(postcode: String, distance: String) {
        repository.storeOnboardingData(postcode, distance)
    }
}
