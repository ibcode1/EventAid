package com.ib.eventaid.common.data.api

import com.ib.eventaid.common.data.api.model.ApiPaginatedPerformers
import retrofit2.http.GET
import retrofit2.http.Query

interface PerformerAidApi {

    @GET(ApiConstants.PERFORMERS_ENDPOINT)
    suspend fun getPerformers(
        @Query(ApiParameters.PAGE) pageToLoad:Int,
        @Query(ApiParameters.PER_PAGE) pageSize:Int,
        //@Query(ApiParameters.POSTAL_CODE) postcode:String,
        //@Query(ApiParameters.RANGE) maxRange:String,
        @Query(ApiParameters.CLIENT_ID) client_id:String
    ): ApiPaginatedPerformers
}

