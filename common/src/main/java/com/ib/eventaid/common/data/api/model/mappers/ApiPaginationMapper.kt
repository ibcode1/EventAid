package com.ib.eventaid.common.data.api.model.mappers

import com.ib.eventaid.common.data.api.model.ApiPagination
import com.ib.eventaid.common.domain.model.pagination.GeoLocation
import com.ib.eventaid.common.domain.model.pagination.Pagination
import javax.inject.Inject

class ApiPaginationMapper @Inject constructor(
    private val apiGeoLocationMapper: ApiGeoLocationMapper
) : ApiMapper<ApiPagination?, Pagination> {
    override fun mapToDomain(apiEntity: ApiPagination?): Pagination {
        return Pagination(
            page = apiEntity?.page ?: 0, //NB Current page
            totalPages = apiEntity?.total ?: 0,
            //geolocation = apiGeoLocationMapper.mapToDomain(apiEntity)
        )
    }
}

class ApiGeoLocationMapper @Inject constructor():ApiMapper<ApiPagination?,GeoLocation>{
    override fun mapToDomain(apiEntity: ApiPagination?): GeoLocation {
        return GeoLocation(
            range = apiEntity?.geolocation?.range.orEmpty()
        )
    }

}