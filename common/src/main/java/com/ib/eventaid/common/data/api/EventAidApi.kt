package com.ib.eventaid.common.data.api

import com.ib.eventaid.common.data.api.model.ApiPaginatedEvents
import com.ib.eventaid.common.data.api.model.ApiPaginatedPerformers
import retrofit2.http.GET
import retrofit2.http.Query

interface EventAidApi {

    @GET(ApiConstants.EVENTS_ENDPOINT)
    suspend fun getNearbyEvents(
        @Query(ApiParameters.PAGE) pageToLoad:Int,
        @Query(ApiParameters.PER_PAGE) pageSize:Int,
        @Query(ApiParameters.POSTAL_CODE) postcode:String,
        @Query(ApiParameters.RANGE) maxRange:String,
        @Query(ApiParameters.CLIENT_ID) client_id:String
    ): ApiPaginatedEvents

    @GET(ApiConstants.PERFORMERS_ENDPOINT)
    suspend fun getPerformers(
        @Query(ApiParameters.PAGE) pageToLoad:Int,
        @Query(ApiParameters.PER_PAGE) pageSize:Int,
        @Query(ApiParameters.POSTAL_CODE) postcode:String,
        @Query(ApiParameters.RANGE) maxRange:String,
        @Query(ApiParameters.CLIENT_ID) client_id:String
    ):ApiPaginatedEvents
            //ApiPaginatedPerformers

    @GET(ApiConstants.EVENTS_ENDPOINT)
    suspend fun searchEventsBy(
        @Query(ApiParameters.TITLE) q:String,
        //@Query(ApiParameters.VENUE) venue:String,
        @Query(ApiParameters.TYPE) type:String,
        //@Query(ApiParameters.TAXONOMIES) taxonomies:String,
        //@Query(ApiParameters.PERFORMER) performer:String,
        @Query(ApiParameters.PAGE) pageToLoad: Int,
        @Query(ApiParameters.PER_PAGE) pageSize:Int,
        // @Query(ApiParameters.POSTAL_CODE) postcode: String,
        @Query(ApiParameters.RANGE) maxRange: String,
        @Query(ApiParameters.CLIENT_ID) client_id:String
    ):ApiPaginatedEvents
}