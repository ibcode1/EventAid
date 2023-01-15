package com.ib.eventaid.common.data.api.model.mappers

import com.ib.eventaid.common.data.api.model.ApiVenue
import com.ib.eventaid.common.domain.model.venue.Venue
import javax.inject.Inject

class ApiAddressMapper @Inject constructor() : ApiMapper<ApiVenue?, Venue.Address> {
    override fun mapToDomain(apiEntity: ApiVenue?): Venue.Address {
        return Venue.Address(
            address1 = apiEntity?.address.orEmpty(),
            address2 = apiEntity?.extendedAddress.orEmpty(),
            city = apiEntity?.city.orEmpty(),
            state = apiEntity?.state.orEmpty(),
            postalCode = apiEntity?.postalCode.orEmpty(),
            country = apiEntity?.country.orEmpty()
        )
    }
}
