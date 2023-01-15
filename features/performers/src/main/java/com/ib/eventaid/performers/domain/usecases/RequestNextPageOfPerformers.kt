package com.ib.eventaid.performers.domain.usecases

import com.ib.eventaid.common.domain.NoMorePerformersException
import com.ib.eventaid.common.domain.model.pagination.Pagination
import com.ib.eventaid.common.domain.repositories.PerformerRepository
import com.ib.eventaid.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestNextPageOfPerformers @Inject constructor(
    private val  performerRepository: PerformerRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(
        pageToLoad: Int,
        pageSize: Int = Pagination.DEFAULT_PAGE_SIZE
    ):Pagination{
        return withContext(dispatchersProvider.io()){
            val (performers, pagination) = performerRepository.requestMorePerformers(pageToLoad,pageSize)

            if (performers.isEmpty()){
                throw NoMorePerformersException("No Performers :(")
            }
            performerRepository.storePerformers(performers)
            return@withContext pagination
        }
    }
}