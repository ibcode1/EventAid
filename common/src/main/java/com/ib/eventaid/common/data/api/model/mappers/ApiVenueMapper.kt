package com.ib.eventaid.common.data.api.model.mappers

import com.ib.eventaid.common.data.api.model.ApiVenue
import com.ib.eventaid.common.domain.model.venue.Venue
import javax.inject.Inject

class ApiVenueMapper @Inject constructor(
    private val apiAddressMapper: ApiAddressMapper,
    private val apiVenueInfoMapper: ApiVenueInfoMapper,
    private val apiVenueLocationMapper: ApiVenueLocationMapper
):ApiMapper<ApiVenue?,Venue> {
    override fun mapToDomain(apiEntity: ApiVenue?): Venue {
        return Venue(
            id = apiEntity?.id?:0,
            name = apiEntity?.name.orEmpty(),
            timeZone = apiEntity?.timezone.orEmpty(),
            address = apiAddressMapper.mapToDomain(apiEntity),
            info = apiVenueInfoMapper.mapToDomain(apiEntity),
            location = apiVenueLocationMapper.mapToDomain(apiEntity)
        )
    }
}