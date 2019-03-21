package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.repo.NewPropertyRepository
import com.openclassrooms.realestatemanager.repo.PropertyRepository
import com.openclassrooms.realestatemanager.repo.impl.NewPropertyManager
import com.openclassrooms.realestatemanager.repo.impl.PropertyManager
import dagger.Module
import dagger.Provides

@Module
object RepositoryModule {

    @Provides
    fun provideProperty(): PropertyRepository = PropertyManager

    @Provides
    fun provideNewProperty(): NewPropertyRepository = NewPropertyManager

}