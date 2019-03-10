package com.pet.kaleidoscope

import android.util.Log
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Dmitry Borodin on 3/10/19.
 */
@RunWith(AndroidJUnit4::class)
class EncodingTests {

    @Test
    fun shouldDecodeTheSameAsOriginal() {
        val text = "tralala with space"

        val encoded = text.encode()

        assertEquals(text, encoded.decode())
    }

    @Ignore
    @Test
    fun seeEncoded() {
        Log.e("encode", Constants.FLICKR_API.encode())
        Log.e("encode", Constants.FLICKR_SECRET.encode())
    }
}