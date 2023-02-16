package com.ib.eventaid.common.data.cache.model.cachedEvent

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ib.eventaid.common.data.cache.model.cachedTaxonomy.CachedTaxonomy
import com.ib.eventaid.common.data.cache.model.cachedVenue.CachedVenue
import com.ib.eventaid.common.domain.model.event.Event
import com.ib.eventaid.common.domain.model.event.Media
import com.ib.eventaid.common.domain.model.event.details.Details
import com.ib.eventaid.common.domain.model.event.details.EventWithDetails
import com.ib.eventaid.common.utils.DateTimeUtils

@Entity(
    tableName = "events",
    foreignKeys = [

        ForeignKey(
            entity = CachedVenue::class,
            parentColumns = ["venueId"],
            childColumns = ["venueId"],
            onDelete = ForeignKey.CASCADE
        ),

//        ForeignKey(
//            entity = CachedEventPerformer::class,
//            parentColumns = ["performerId"],
//            childColumns = ["performerId"],
//            onDelete = ForeignKey.CASCADE
//        ),

        /*ForeignKey(
            entity = CachedTaxonomy::class,
            parentColumns = ["taxonomyId"],
            childColumns = ["taxonomyId"],
            onDelete = ForeignKey.CASCADE
        )*/
    ],
    indices = [
        //Index("performerId"),
        Index("venueId"),
        //Index("taxonomyId")
    ]
)

data class CachedEventWithDetails(
    @PrimaryKey val eventId: Int,
    val venueId: Int,
    val performerId: List<Int>,
    val title: String,
    val type: String,
    val description: String,
    val averagePrice: Int,
    val highestPrice: Int,
    val listingCount: Int,
    val lowestPrice: Int,
    val medianPrice: Int,
    val visibleListingCount: Int,
    val lowestPriceGoodDeals: Int,
    val lowestSgBasePrice: Int,
    val lowestSgBasePriceGoodDeals: Int,
    val dateTimeLocal: String,
    val visibleUntilUtc: String,
    val venueName: String,
    val performerName:List<String>
) {
    companion object {
        fun fromDomain(domainModel: EventWithDetails): CachedEventWithDetails {
            val details = domainModel.details
            //val taxonomy = details.taxonomy
            val venue = details.venue
            val performers = details.performers
            val stats = details.stats

            return CachedEventWithDetails(
                eventId = domainModel.id,
                venueId = venue.id,
                title = domainModel.title,
                description = details.description,
                averagePrice = stats.averagePrice,
                highestPrice = stats.highestPrice,
                listingCount = stats.listingCount,
                lowestPrice = stats.lowestPrice,
                medianPrice = stats.medianPrice,
                visibleListingCount = stats.visibleListingCount,
                lowestSgBasePriceGoodDeals = stats.lowestSgBasePriceGoodDeals,
                lowestSgBasePrice = stats.lowestSgBasePrice,
                lowestPriceGoodDeals = stats.lowestPriceGoodDeals,
                dateTimeLocal = domainModel.dateTimeLocal.toString(),
                visibleUntilUtc = domainModel.visibleUntilUtc.toString(),
                venueName = venue.name,
                type = domainModel.type,
                performerId = performers.map { it.id },
                performerName = performers.map { it.name }
            )
        }
    }

    fun toDomain(
        venue: CachedVenue,
        performer: List<CachedEventPerformer>,
        taxonomy: List<CachedTaxonomy>,
        stats: CachedStats,
        images: List<CachedImage>
    ): EventWithDetails {
        return EventWithDetails(
            id = eventId,
            title = title,
            details = mapDetails(venue, taxonomy, performer, stats),
            dateTimeLocal = DateTimeUtils.parse(dateTimeLocal),
            visibleUntilUtc = DateTimeUtils.parse(visibleUntilUtc),
            type = type,
            media = Media(
                images = images.map { it.toDomain() }
            )
        )
    }

    fun toEventDomain(
        images: List<CachedImage>,
    ): Event {
        return Event(
            id = eventId,
            title = title,
            dateTimeLocal = DateTimeUtils.parse(dateTimeLocal),
            visibleUntilUtc = DateTimeUtils.parse(visibleUntilUtc),
            //image = images.map { it.image }
            //image = image.image
            media = Media(images = images.map { it.toDomain() }),

            )
    }

    private fun mapDetails(
        venue: CachedVenue,
        taxonomy: List<CachedTaxonomy>,
        performer: List<CachedEventPerformer>,
        stats: CachedStats
    ): Details {
        return Details(
            description = description,
            venue = venue.toDomain(),
            performers = performer.map { it.toDomain() },
            taxonomy = taxonomy.map {it.toDomain()},
            stats = stats.toDomain()
        )
    }
}