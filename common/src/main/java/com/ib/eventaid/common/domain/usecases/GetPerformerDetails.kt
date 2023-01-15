package com.ib.eventaid.common.domain.usecases

import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.domain.repositories.PerformerRepository
import com.ib.eventaid.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPerformerDetails @Inject constructor(
    private val performerRepository: PerformerRepository,
    private val dispatchersProvider: DispatchersProvider,
) {

    suspend operator fun invoke(performerId:Int):Performer{
        return withContext(dispatchersProvider.io()){
            performerRepository.getPerformer(performerId)
        }
    }
}