package com.openclassrooms.realestatemanager.ui.main

import com.openclassrooms.realestatemanager.di.RepositoryComponent
import com.openclassrooms.realestatemanager.repo.PropertyRepository
import com.openclassrooms.realestatemanager.ui.base.BaseTranslator
import com.openclassrooms.realestatemanager.utils.log
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class  MainTranslator: BaseTranslator<Action, ActionUiModel>()  {

    @Inject
    lateinit var propertyRepository: PropertyRepository

    override fun inject(component: RepositoryComponent) {
        component.inject(this)
    }

    override fun Observable<Action>.reduce(): Observable<ActionUiModel> {

        return Observable.mergeArray(

                ofType<Action.GetAllProperty>().requestForGetAllProperty()

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
                    .map {
                        ActionUiModel.GetAllPropertyModel(it)
                    }
        }
    }




}