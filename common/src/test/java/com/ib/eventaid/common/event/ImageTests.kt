package com.ib.eventaid.common.event

import com.ib.eventaid.common.domain.model.event.Media
import org.junit.Test
import org.junit.Assert.assertEquals


class ImageTests {

    private val hugeImage = "hugeImage"
    private val x320 = "x320Image"
    private val invalidImage = "" // what's tested in Photo.isValidImage()

    @Test
    fun image_getSmallestAvailableImage_hasHugeImage() {
        //Given
        val image = Media.Image(hugeImage, x320)
        val expectedValue = hugeImage

        //When
        val smallestImage = image.getSmallestAvailableImage()

        //Then
        assertEquals(smallestImage, expectedValue)
    }

    @Test
    fun image_getSmallestAvailableImage_noHugeImage_hasX320Image() {
        //Given
        val image = Media.Image(invalidImage, x320)
        val expectedValue = x320

        //when
        val smallestImage = image.getSmallestAvailableImage()

        //Then
        assertEquals(smallestImage, expectedValue)
    }

    @Test
    fun image_getSmallestAvailableImage_noImage() {
        //Given
        val image = Media.Image(invalidImage, invalidImage)
        val expectedValue = Media.Image.NO_SIZE_AVAILABLE

        //When
        val smallestImage = image.getSmallestAvailableImage()

        //Then
        assertEquals(smallestImage, expectedValue)
    }
}

//NB:Regarding the images provided by the Api,Most of the events seems to have an Image call Api.Also
//Most of the events seems to have one image(which is the huge image) and not a list.Infact the huge image is enough
//however for testing sake x320 image was added. Hence the smallest available image is huge :(.