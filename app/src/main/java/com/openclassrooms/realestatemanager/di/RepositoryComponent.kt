package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.ui.createNewProperty.NewPropertyTranslator
import com.openclassrooms.realestatemanager.ui.property.PropertyTranslator
import dagger.Component

@Component(modules = [RepositoryModule::class])
interface RepositoryComponent {

    interface Injectable {
    fun inject(component: RepositoryComponent)
    }

    fun inject(mainTranslator: PropertyTranslator)

    fun inject(newProperty: NewPropertyTranslator)

}