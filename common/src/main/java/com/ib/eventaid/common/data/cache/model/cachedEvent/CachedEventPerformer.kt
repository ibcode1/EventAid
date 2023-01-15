package com.ib.eventaid.common.data.cache.model.cachedEvent

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ib.eventaid.common.domain.model.event.details.EventWithDetails
import com.ib.eventaid.common.domain.model.performer.Performer

@Entity(
    tableName = "performers"
)
data class CachedEventPerformer(
    @PrimaryKey
    val performerId: Int,
    val eventId: Int =0,
    val name: String,
    val awayTeam: Boolean,
    val hasUpcomingEvents: Boolean,
    val homeTeam: Boolean,
    val image: String,
    val url: String,
    val numUpcomingEvents: Int,
    val score: Double,
    val popularity: Int,
    val slug: String,
    val type: String,
    //val media: Media

) {

    companion object {
        fun fromDomain(domainModel: Performer): CachedEventPerformer {

            return CachedEventPerformer(
                performerId = domainModel.id,
                name = domainModel.name,
                awayTeam = domainModel.awayTeam,
                homeTeam = domainModel.homeTeam,
                hasUpcomingEvents = domainModel.hasUpcomingEvents,
                image = domainModel.image,
                numUpcomingEvents = domainModel.numUpcomingEvents,
                popularity = domainModel.popularity,
                score = domainModel.score,
                slug = domainModel.slug,
                type = domainModel.type,
                url = domainModel.url,
            )
        }

        fun fromEventDomain(domainModel: Performer,eventId:Int):CachedEventPerformer{
            return CachedEventPerformer(
                performerId = domainModel.id,
                name = domainModel.name,
                awayTeam = domainModel.awayTeam,
                homeTeam = domainModel.homeTeam,
                hasUpcomingEvents = domainModel.hasUpcomingEvents,
                image = domainModel.image,
                numUpcomingEvents = domainModel.numUpcomingEvents,
                popularity = domainModel.popularity,
                score = domainModel.score,
                slug = domainModel.slug,
                type = domainModel.type,
                url = domainModel.url,
                eventId = eventId
            )
        }
    }

    fun toDomain(): Performer {
        return Performer(
            id = performerId,
            name = name,
            awayTeam = awayTeam,
            hasUpcomingEvents = hasUpcomingEvents,
            homeTeam = homeTeam,
            image = image,
            url = url,
            numUpcomingEvents = numUpcomingEvents,
            score = score,
            popularity = popularity,
            slug = slug,
            type = type,
        )
    }

    fun toPerformerDomain(): Performer {
        return Performer(
            awayTeam = false,
            hasUpcomingEvents = true,
            homeTeam = true,
            id = performerId,
            image = image,
            name = name,
            numUpcomingEvents = numUpcomingEvents,
            popularity = popularity,
            score = score,
            slug = slug,
            type = type,
            url = type
        )
    }
}