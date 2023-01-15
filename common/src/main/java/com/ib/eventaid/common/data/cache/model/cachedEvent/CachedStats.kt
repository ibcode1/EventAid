package com.ib.eventaid.common.data.cache.model.cachedEvent

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ib.eventaid.common.domain.model.event.details.Stats

@Entity(
    tableName = "stats",
    foreignKeys = [
        ForeignKey(
            entity = CachedEventWithDetails::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("eventId")]
)
data class CachedStats(
    @PrimaryKey(autoGenerate = true)
    val statsId: Long = 0,
    val eventId: Int,
    val averagePrice: Int,
    val highestPrice: Int,
    val lowestPrice: Int,
    val listingCount: Int,
    val lowestPriceGoodDeals: Int,
    val lowestSgBasePrice: Int,
    val lowestSgBasePriceGoodDeals: Int,
    val medianPrice: Int,
    val visibleListingCount: Int

) {

    companion object {
        fun fromDomain(eventId: Int, stats: Stats): CachedStats {
            val (
                averagePrice,
                highestPrice,
                listingCount,
                lowestPrice,
                medianPrice,
                visibleListingCount,
                lowestPriceGoodDeals,
                lowestSgBasePrice,
                lowestSgBasePriceGoodDeals,
            ) = stats

            return CachedStats(
                eventId = eventId,
                averagePrice = averagePrice,
                highestPrice = highestPrice,
                lowestPrice = lowestPrice,
                listingCount = listingCount,
                lowestSgBasePriceGoodDeals = lowestSgBasePriceGoodDeals,
                lowestSgBasePrice = lowestSgBasePrice,
                medianPrice = medianPrice,
                lowestPriceGoodDeals = lowestPriceGoodDeals,
                visibleListingCount = visibleListingCount
            )
        }
    }

    fun toDomain(): Stats = Stats(
        averagePrice,
        highestPrice,
        lowestPrice,
        listingCount,
        lowestPriceGoodDeals,
        lowestSgBasePrice,
        lowestSgBasePriceGoodDeals,
        medianPrice,
        visibleListingCount
    )
}