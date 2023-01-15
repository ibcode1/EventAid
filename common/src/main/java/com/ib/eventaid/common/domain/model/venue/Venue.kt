package com.ib.eventaid.common.domain.model.venue

data class Venue(
    val id: Int,
    val name: String,
    val address: Address,
    val timeZone: String,
    val info:Info,
    val location: Location
) {
    data class Address(
        val address1: String,
        val address2: String,
        val city: String,
        val state: String,
        val postalCode: String,
        val country: String
    )

    data class Info(
        val capacity: Int,
        val numUpcomingEvents: Int,
        val venueScore: Double,
        val url: String,
        val hasUpcomingEvents: Boolean
    )

    data class Location(
        val lat:Double,
        val lon:Double
    )
}
