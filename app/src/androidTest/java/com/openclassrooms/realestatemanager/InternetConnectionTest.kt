package com.openclassrooms.realestatemanager

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.openclassrooms.realestatemanager.utils.Utils
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InternetConnectionTest {

    @Test
    fun checkInternetConnection() {
        assertEquals(true, Utils.isInternetAvailable(InstrumentationRegistry.getContext()))
    }
}