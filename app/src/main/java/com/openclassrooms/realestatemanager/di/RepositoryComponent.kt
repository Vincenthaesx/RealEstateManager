package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.ui.main.MainTranslator
import dagger.Component

@Component(modules = [RepositoryModule::class])
interface RepositoryComponent {

    interface Injectable {
        fun inject(component: RepositoryComponent)
    }

    fun inject(mainTranslator: MainTranslator)

}