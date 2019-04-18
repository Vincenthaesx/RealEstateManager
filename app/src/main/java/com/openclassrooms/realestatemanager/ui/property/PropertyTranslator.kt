package com.openclassrooms.realestatemanager.ui.property

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.di.RepositoryComponent
import com.openclassrooms.realestatemanager.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.repo.PropertyRepository
import com.openclassrooms.realestatemanager.ui.base.BaseTranslator
import com.openclassrooms.realestatemanager.utils.log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PropertyTranslator : BaseTranslator<Action, ActionUiModel>() {

    @Inject
    lateinit var propertyRepository: PropertyRepository

    override fun inject(component: RepositoryComponent) {
        component.inject(this)
    }

    override fun Observable<Action>.reduce(): Observable<ActionUiModel> {

        return Observable.mergeArray(

                ofType<Action.GetAllProperty>().requestForGetAllProperty(),
                ofType<Action.GetProperty>().requestForGetProperty(),
                ofType<Action.GetPropertyBySearch>().requestForGetPropertyBySearch()

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
                    .map<ActionUiModel> { action ->
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

    private fun Observable<Action.GetPropertyBySearch>.requestForGetPropertyBySearch(): Observable<ActionUiModel> {
        return flatMap<ActionUiModel> { action ->
            val query = SimpleSQLiteQuery(action.queryToConvert, action.args.toArray())
            Log.e("GET_ESTATES_BY_SEARCH", "Query to execute : ${query.sql}")
            action.args.forEach {
                if (it is Long) Log.e("GET_ESTATES_BY_SEARCH", "Args : ${SimpleDateFormat("dd/MM/yyyy").format(Date(it))}")
                else Log.e("GET_ESTATES_BY_SEARCH", "Args : $it")
            }
            RealEstateDatabase.realEstateDatabase.propertyDao().getItemsBySearch(query)
                    .toObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

                    .map {
                        ActionUiModel.GetPropertyBySearchModel(it)
                    }
        }
    }
}