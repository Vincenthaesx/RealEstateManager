package com.openclassrooms.realestatemanager

import android.app.Application
import android.content.Context

class RealEstateManagerApplication : Application(){

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        RealEstateManagerApplication.applicationContext()

    }

    companion object {
        private var instance: RealEstateManagerApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }

    }
}