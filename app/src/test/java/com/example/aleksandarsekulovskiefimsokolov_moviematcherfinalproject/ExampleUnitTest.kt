package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject

import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages.getProfilePicture
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun test_valid_index() {
        val index = 2
        val expectedDrawable = R.drawable.moana
        val actual = getProfilePicture(index)
        assertEquals(expectedDrawable, actual)
    }

    @Test
    fun test_invalid_index() {
        val invalidIndex = 999
        val expected = R.drawable.defaultprofile
        val actual = getProfilePicture(invalidIndex)
        assertEquals(expected, actual)
    }


}