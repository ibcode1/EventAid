package com.ib.eventaid.common.data.api.model.mappers

import com.ib.eventaid.common.data.api.model.ApiEvent
import com.ib.eventaid.common.data.api.model.ApiPerformer
import com.ib.eventaid.common.domain.model.event.Event
import com.ib.eventaid.common.domain.model.event.Media
import com.ib.eventaid.common.domain.model.performer.Performer
import javax.inject.Inject

class ApiPerformerMapper @Inject constructor(
    private val apiImageMapper: ApiImageMapper
) : ApiMapper<ApiPerformer?, Performer> {
    override fun mapToDomain(apiEntity: ApiPerformer?): Performer {
        return Performer(
            awayTeam = apiEntity?.awayTeam ?: false,
            homeTeam = apiEntity?.homeTeam ?: true,
            hasUpcomingEvents = apiEntity?.hasUpcomingEvents ?: true,
            id = apiEntity?.id ?: throw MappingException("Performer ID cannot be null"),
            image = apiEntity.image.orEmpty(),
            name = apiEntity.name.orEmpty(),
            numUpcomingEvents = apiEntity.numUpcomingEvents ?: 0,
            popularity = apiEntity.popularity ?: 0,
            score = apiEntity.score ?: 0.0,
            slug = apiEntity.slug.orEmpty(),
            type = apiEntity.type.orEmpty(),
            url = apiEntity.url.orEmpty(),
            //media = apiImageMapper.mapToDomain(apiEntity.images)
            )
    }
}