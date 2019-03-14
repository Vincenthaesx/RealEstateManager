package com.openclassrooms.realestatemanager.ui.property

import com.openclassrooms.realestatemanager.di.RepositoryComponent
import com.openclassrooms.realestatemanager.repo.PropertyRepository
import com.openclassrooms.realestatemanager.ui.base.BaseTranslator
import com.openclassrooms.realestatemanager.utils.log
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class  PropertyTranslator: BaseTranslator<Action, ActionUiModel>()  {

    @Inject
    lateinit var propertyRepository: PropertyRepository

    override fun inject(component: RepositoryComponent) {
        component.inject(this)
    }

    override fun Observable<Action>.reduce(): Observable<ActionUiModel> {

        return Observable.mergeArray(

                ofType<Action.GetAllProperty>().requestForGetAllProperty(),
                ofType<Action.GetProperty>().requestForGetProperty(),
                ofType<Action.AddNewProperty>().requestForAddNewProperty()

                        .onErrorReturn {
                            it.log()
                            ActionUiModel.Error(it.message)
                        }
        )
    }

    // ---------------------------

    private fun Observable<Action.GetAllProperty>.requestForGetAllProperty(): Observable<ActionUiModel> {
        return flatMap {
            propertyRepository.getPropertyList()
                    .map<ActionUiModel> {action ->
                        ActionUiModel.GetAllPropertyModel(action)
                    }
                    .startWith(ActionUiModel.Loading(true))
                    .concatWith(Observable.just(ActionUiModel.Loading(false)))
        }
    }

    private fun Observable<Action.GetProperty>.requestForGetProperty(): Observable<ActionUiModel> {
        return flatMap { action ->
            propertyRepository.getProperty(action.id)
                    .map<ActionUiModel> {
                        ActionUiModel.GetPropertyModel(it)
                    }
                    .startWith(ActionUiModel.Loading(true))
                    .concatWith(Observable.just(ActionUiModel.Loading(false)))
        }
    }

    private fun Observable<Action.AddNewProperty>.requestForAddNewProperty(): Observable<ActionUiModel> {
        return flatMap { action ->
            propertyRepository.addNewProperty(action.newProperty)
                    .map {
                        ActionUiModel.AddNewPropertyModel(it)
                    }
        }
    }

}