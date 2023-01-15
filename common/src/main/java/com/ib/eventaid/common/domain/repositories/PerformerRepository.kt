package com.ib.eventaid.common.domain.repositories

import com.ib.eventaid.common.domain.model.pagination.PaginatedPerformers
import com.ib.eventaid.common.domain.model.performer.Performer
import io.reactivex.Flowable

interface PerformerRepository {

    fun getPerformers(): Flowable<List<Performer>>
    suspend fun requestMorePerformers(pageToLoad: Int, numberOfItems: Int): PaginatedPerformers
    suspend fun storePerformers(performers: List<Performer>)
    //suspend fun storeAllPerformers(performers: List<Performer>)

    suspend fun getPerformer(performerId: Int): Performer
}