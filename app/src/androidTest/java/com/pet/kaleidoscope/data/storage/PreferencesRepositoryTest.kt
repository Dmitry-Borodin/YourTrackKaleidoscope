package com.pet.kaleidoscope.data.storage

import androidx.test.runner.AndroidJUnit4
import com.pet.kaleidoscope.App
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Dmitry Borodin on 3/15/19.
 */
@RunWith(AndroidJUnit4::class)
class PreferencesRepositoryTest {

    val repository: FlickrRepository = FlickrRepositoryImpl(App.instance, "testFile")

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {
        (repository as FlickrRepositoryImpl).clearAllEntries()
    }

    @Test
    fun shouldRestopre() {
        val original = FlickrOAuthData("token", "token secret")

        repository.oauthFlickrCredentials = original

        assertEquals(original, repository.oauthFlickrCredentials)
    }
}