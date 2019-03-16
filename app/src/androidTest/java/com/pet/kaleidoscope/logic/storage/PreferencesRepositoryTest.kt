package com.pet.kaleidoscope.logic.storage

import androidx.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject

/**
 * @author Dmitry Borodin on 3/15/19.
 */
@RunWith(AndroidJUnit4::class)
class PreferencesRepositoryTest : KoinTest {

    private val repository: FlickrRepository by inject()

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