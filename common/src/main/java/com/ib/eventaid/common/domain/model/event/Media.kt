package com.ib.eventaid.common.domain.model.event

data class Media(
    val images: List<Image>
) {
    companion object {
        const val NO_MEDIA = ""
    }

    fun getFirstSmallestAvailableImage(): String {
        if (images.isEmpty()) return NO_MEDIA

        return images.first().getSmallestAvailableImage()
    }

    data class Image(
        val huge: String,
        val x320: String
        //huge is smaller by the way.Don't know why they named it so.lol
    ) {

        companion object {
            const val NO_SIZE_AVAILABLE = ""
        }

        fun getSmallestAvailableImage(): String {
            return when {
                isValidImage(huge) -> huge
                isValidImage(x320) -> x320
                else -> NO_SIZE_AVAILABLE
            }
        }

        private fun isValidImage(image: String): Boolean {
            return image.isNotEmpty()
        }
    }
}
