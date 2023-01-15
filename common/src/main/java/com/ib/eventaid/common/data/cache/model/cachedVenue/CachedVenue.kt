package com.ib.eventaid.common.data.cache.model.cachedVenue

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ib.eventaid.common.domain.model.venue.Venue

@Entity(tableName = "venues")
data class CachedVenue(
    @PrimaryKey(autoGenerate = false)
    val venueId: Int,
    val name: String,
    val timeZone: String,
    val address1: String,
    val address2: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String,
    val capacity: Int,
    val numUpcomingEvents: Int,
    val venueScore: Double,
    val url: String,
    val hasUpcomingEvents: Boolean,
    val lat:Double,
    val lon:Double
) {
    companion object {
        fun fromDomain(domainModel: Venue): CachedVenue {
            val address = domainModel.address
            val info = domainModel.info
            val location = domainModel.location

            return CachedVenue(
                venueId = domainModel.id,
                name = domainModel.name,
                timeZone = domainModel.timeZone,
                address1 = address.address1,
                address2 = address.address2,
                city = address.city,
                state = address.state,
                postalCode = address.postalCode,
                country = address.country,
                capacity = info.capacity,
                hasUpcomingEvents = info.hasUpcomingEvents,
                numUpcomingEvents = info.numUpcomingEvents,
                url = info.url,
                venueScore = info.venueScore,
                lat = location.lat,
                lon = location.lon
            )
        }
    }

    fun toDomain(): Venue {
        return Venue(
            venueId,
            name,
            Venue.Address(
                address1, address2, city, state, postalCode, country),
            timeZone,
            Venue.Info(
                capacity, numUpcomingEvents, venueScore, url, hasUpcomingEvents),
            Venue.Location(
                lat,lon))
    }
}
