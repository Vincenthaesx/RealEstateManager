package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.ui.map.MapTranslator
import com.openclassrooms.realestatemanager.ui.newProperty.NewPropertyTranslator
import com.openclassrooms.realestatemanager.ui.property.PropertyTranslator
import com.openclassrooms.realestatemanager.ui.updateProperty.UpdatePropertyTranslator
import dagger.Component

@Component(modules = [RepositoryModule::class])
interface RepositoryComponent {

    interface Injectable {
        fun inject(component: RepositoryComponent)
    }

    fun inject(mainTranslator: PropertyTranslator)

    fun inject(newProperty: NewPropertyTranslator)

    fun inject(updateProperty: UpdatePropertyTranslator)

    fun inject(mapProperty: MapTranslator)

}