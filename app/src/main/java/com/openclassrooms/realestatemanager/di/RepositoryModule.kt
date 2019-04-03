package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.repo.MapRepository
import com.openclassrooms.realestatemanager.repo.NewPropertyRepository
import com.openclassrooms.realestatemanager.repo.PropertyRepository
import com.openclassrooms.realestatemanager.repo.UpdatePropertyRepository
import com.openclassrooms.realestatemanager.repo.impl.MapManager
import com.openclassrooms.realestatemanager.repo.impl.NewPropertyManager
import com.openclassrooms.realestatemanager.repo.impl.PropertyManager
import com.openclassrooms.realestatemanager.repo.impl.UpdatePropertyManager
import dagger.Module
import dagger.Provides

@Module
object RepositoryModule {

    @Provides
    fun provideProperty(): PropertyRepository = PropertyManager

    @Provides
    fun provideNewProperty(): NewPropertyRepository = NewPropertyManager

    @Provides
    fun provideUpdateProperty(): UpdatePropertyRepository = UpdatePropertyManager

    @Provides
    fun provideMap(): MapRepository = MapManager

}