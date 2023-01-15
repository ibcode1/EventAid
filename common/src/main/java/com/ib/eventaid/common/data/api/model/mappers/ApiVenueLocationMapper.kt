package com.ib.eventaid.common.data.api.model.mappers

import com.ib.eventaid.common.data.api.model.ApiVenue
import com.ib.eventaid.common.domain.model.venue.Venue
import javax.inject.Inject

class ApiVenueLocationMapper @Inject constructor():ApiMapper<ApiVenue?, Venue.Location> {
    override fun mapToDomain(apiEntity: ApiVenue?): Venue.Location {
        return Venue.Location(
            lat = apiEntity?.location?.lat?:0.0,
            lon = apiEntity?.location?.lon?:0.0)
    }
}