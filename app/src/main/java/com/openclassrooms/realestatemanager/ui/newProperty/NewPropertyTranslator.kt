package com.openclassrooms.realestatemanager.ui.newProperty

import com.openclassrooms.realestatemanager.di.RepositoryComponent
import com.openclassrooms.realestatemanager.repo.NewPropertyRepository
import com.openclassrooms.realestatemanager.ui.base.BaseTranslator
import com.openclassrooms.realestatemanager.utils.log
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class NewPropertyTranslator : BaseTranslator<Action, ActionUiModel>() {

    @Inject
    lateinit var newPropertyRepository: NewPropertyRepository

    override fun inject(component: RepositoryComponent) {
        component.inject(this)
    }

    override fun Observable<Action>.reduce(): Observable<ActionUiModel> {

        return Observable.mergeArray(

                ofType<Action.AddNewProperty>().requestForAddNewProperty()

                        .onErrorReturn {
                            it.log()
                            ActionUiModel.Error(it.message)
                        }
        )
    }

    // ---------------------------

    private fun Observable<Action.AddNewProperty>.requestForAddNewProperty(): Observable<ActionUiModel> {
        return flatMap { action ->
            newPropertyRepository.addNewProperty(action.newProperty)
                    .map {
                        ActionUiModel.AddNewPropertyModel(it)
                    }
        }
    }

}