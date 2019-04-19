package com.openclassrooms.realestatemanager

import android.app.Application
import android.content.Context
import com.openclassrooms.realestatemanager.di.DaggerRepositoryComponent
import com.openclassrooms.realestatemanager.di.RepositoryComponent
import com.openclassrooms.realestatemanager.di.RepositoryModule

class RealEstateManagerApplication : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        applicationContext()

        repoComponent = DaggerRepositoryComponent.builder().repositoryModule(RepositoryModule)
                .build()

    }

    companion object {
        lateinit var repoComponent: RepositoryComponent

        private lateinit var instance: RealEstateManagerApplication

        fun applicationContext(): Context {
            return instance.applicationContext
        }

    }
}