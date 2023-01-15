package com.ib.eventaid.common.data

import com.ib.eventaid.common.data.api.ApiConstants
import com.ib.eventaid.common.data.api.PerformerAidApi
import com.ib.eventaid.common.data.api.model.mappers.ApiPaginationMapper
import com.ib.eventaid.common.data.api.model.mappers.ApiPerformerMapper
import com.ib.eventaid.common.data.cache.Cache
import com.ib.eventaid.common.data.cache.model.cachedEvent.CachedEventPerformer
import com.ib.eventaid.common.data.preferences.Preferences
import com.ib.eventaid.common.domain.NetworkException
import com.ib.eventaid.common.domain.model.pagination.PaginatedPerformers
import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.domain.repositories.PerformerRepository
import io.reactivex.Flowable
import retrofit2.HttpException
import javax.inject.Inject

class SeatGeekPerformerRepository @Inject constructor(
    private val api:PerformerAidApi,
    private val cache:Cache,
    private val apiPerformerMapper: ApiPerformerMapper,
    private val apiPaginationMapper: ApiPaginationMapper,
    private val preferences: Preferences
):PerformerRepository {

    private val client = ApiConstants.CLIENT

    override fun getPerformers(): Flowable<List<Performer>> {
        return cache.getPerformers()
            .distinctUntilChanged()
            .map { performerList ->
                performerList.map {
                    it.performer.toPerformerDomain()
                }
            }

    }

    override suspend fun requestMorePerformers(pageToLoad: Int, numberOfItems: Int): PaginatedPerformers {
        val postalCode = preferences.getPostcode()
        val maxRangeMiles = preferences.getMaxDistanceAllowedToGetEvents()

        try {
            val (apiPerformer, apiPagination) = api.getPerformers(
                pageToLoad,
                numberOfItems,
                //postalCode,
                //maxRangeMiles,
                client
            )
            return PaginatedPerformers(
                apiPerformer?.map {
                    apiPerformerMapper.mapToDomain(it)
                }.orEmpty(),
                apiPaginationMapper.mapToDomain(apiPagination)
            )
                }catch (exception:HttpException){
                    throw NetworkException(exception.message?:"Code ${exception.code()}")
                }
    }

    override suspend fun storePerformers(performers: List<Performer>) {
        val performer = performers.map {
            performer -> CachedEventPerformer.fromDomain(performer)
        }
        cache.storePerformer(performer)
    }


    override suspend fun getPerformer(performerId: Int): Performer {
        val performer = cache.getPerformer(performerId)
        return performer.toDomain()
    }
}