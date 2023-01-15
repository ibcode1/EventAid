package com.ib.eventaid.common.data.api.model.mappers

import com.ib.eventaid.common.data.api.model.ApiImageSizes
import com.ib.eventaid.common.domain.model.event.Media
import javax.inject.Inject

class ApiImageMapper @Inject constructor():ApiMapper<ApiImageSizes?,Media.Image> {
    override fun mapToDomain(apiEntity: ApiImageSizes?): Media.Image {
        return Media.Image(
            huge = apiEntity?.huge.orEmpty(),
            x320 = apiEntity?.x320.orEmpty()
        )
    }
}