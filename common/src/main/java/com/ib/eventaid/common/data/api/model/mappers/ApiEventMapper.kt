package com.ib.eventaid.common.data.api.model.mappers

import com.ib.eventaid.common.data.api.model.ApiEvent
import com.ib.eventaid.common.data.api.model.ApiPerformer
import com.ib.eventaid.common.domain.model.event.Media
import com.ib.eventaid.common.domain.model.event.details.Details
import com.ib.eventaid.common.domain.model.event.details.EventWithDetails
import com.ib.eventaid.common.utils.DateTimeUtils
import javax.inject.Inject

class ApiEventMapper @Inject constructor(
    private val apiVenueMapper: ApiVenueMapper,
    private val apiImageMapper: ApiImageMapper,
    private val apiPerformerMapper: ApiPerformerMapper,
    private val apiStatsMapper: ApiStatsMapper,
    private val apiTaxonomyMapper: ApiTaxonomyMapper,
) : ApiMapper<ApiEvent?, EventWithDetails> {
    override fun mapToDomain(apiEntity: ApiEvent?): EventWithDetails {
        return EventWithDetails(
            id = apiEntity?.id ?: throw MappingException("Event ID cannot be null"),
            title = apiEntity.title.orEmpty(),
            dateTimeLocal = DateTimeUtils.parse(apiEntity.datetimeLocal.orEmpty()),
            details = parseEventDetails(apiEntity),
            visibleUntilUtc = DateTimeUtils.parse(apiEntity.visibleUntilUtc.orEmpty()),
            type = apiEntity.type.orEmpty(),
            //image = apiEntity.performers?.map { it.image!! }.orEmpty().toList()
            media = mapMedia(apiEntity)
        )
    }

    private fun parseEventDetails(apiEvent: ApiEvent): Details {
        return Details(
            description = apiEvent.description.orEmpty(),
            performers = apiEvent.performers?.map { apiPerformerMapper.mapToDomain(it) }.orEmpty(),
            stats = apiStatsMapper.mapToDomain(apiEvent.stats),
            //taxonomy = apiEvent.taxonomies?.map { apiTaxonomyMapper.mapToDomain(it) }.orEmpty(),
            venue = apiVenueMapper.mapToDomain(apiEvent.venue)
        )
    }

   private fun mapMedia(apiEvent:ApiEvent):Media{
       return Media(
           images = apiEvent.performers?.map { apiImageMapper.mapToDomain(it.images) }.orEmpty()
       )
   }
}