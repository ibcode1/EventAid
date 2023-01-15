package com.ib.eventaid.common.data.cache.model.cachedEvent

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ib.eventaid.common.domain.model.event.Media


@Entity(
    tableName = "images",
    foreignKeys = [
        ForeignKey(
            entity = CachedEventWithDetails::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("eventId")]
)
data class CachedImage(
    @PrimaryKey(autoGenerate = true)
    val imageId: Long = 0,
    val eventId: Int,
    //val performerId: List<Int>,
    val huge: String,
    val x320: String
) {
    companion object {
        fun fromDomain(
            eventId: Int,
            //performerId: List<Int>,
            image: Media.Image): CachedImage {
            val (huge, x320) = image

            return CachedImage(
                //performerId = performerId,
                eventId = eventId,
                huge = huge,
                x320 = x320
            )
        }
    }

    fun toDomain(): Media.Image = Media.Image(huge, x320)
}


/*@Entity(tableName = "images")
data class CachedImage(
    @PrimaryKey
    val image: String
)*/
