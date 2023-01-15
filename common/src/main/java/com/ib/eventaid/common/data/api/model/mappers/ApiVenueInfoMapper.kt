package com.ib.eventaid.common.data.api.model.mappers

import com.ib.eventaid.common.data.api.model.ApiVenue
import com.ib.eventaid.common.domain.model.venue.Venue
import javax.inject.Inject

class ApiVenueInfoMapper @Inject constructor():ApiMapper<ApiVenue?, Venue.Info> {
    override fun mapToDomain(apiEntity: ApiVenue?): Venue.Info {
        return Venue.Info(
            capacity = apiEntity?.capacity?:0,
            hasUpcomingEvents = apiEntity?.hasUpcomingEvents?:true,
            numUpcomingEvents = apiEntity?.numUpcomingEvents?:0,
            url = apiEntity?.url.orEmpty(),
            venueScore = apiEntity?.score?:0.0
        )
    }
}