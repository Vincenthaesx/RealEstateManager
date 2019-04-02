package com.openclassrooms.realestatemanager.ui.updateProperty

import com.openclassrooms.realestatemanager.di.RepositoryComponent
import com.openclassrooms.realestatemanager.repo.UpdatePropertyRepository
import com.openclassrooms.realestatemanager.ui.base.BaseTranslator
import com.openclassrooms.realestatemanager.utils.log
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class UpdatePropertyTranslator : BaseTranslator<Action, ActionUiModel>() {

    @Inject
    lateinit var updateProperty: UpdatePropertyRepository

    override fun inject(component: RepositoryComponent) {
        component.inject(this)
    }

    override fun Observable<Action>.reduce(): Observable<ActionUiModel> {

        return Observable.mergeArray(

                ofType<Action.GetProperty>().requestForGetProperty(),
                ofType<Action.GetPropertyForUpdate>().requestForGetPropertyForUpdate()

                        .onErrorReturn {
                            it.log()
                            ActionUiModel.Error(it.message)
                        }
        )
    }

    // ---------------------------

    private fun Observable<Action.GetProperty>.requestForGetProperty(): Observable<ActionUiModel> {
        return flatMap { action ->
            updateProperty.getProperty(action.id)
                    .map<ActionUiModel> {
                        ActionUiModel.GetPropertyModel(it)
                    }
        }
    }

    private fun Observable<Action.GetPropertyForUpdate>.requestForGetPropertyForUpdate(): Observable<ActionUiModel> {
        return flatMap { action ->
            updateProperty.updateProperty(action.property)
                    .map {
                        ActionUiModel.GetPropertyModelUpdate(it)
                    }
        }
    }
}