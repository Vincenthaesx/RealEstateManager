package com.openclassrooms.realestatemanager.ui.property

import com.openclassrooms.realestatemanager.models.Property

sealed class Action {

    class GetAllProperty() : Action()

    class GetProperty(val id: Int) : Action()

    class GetPropertyBySearch(val queryToConvert:String, val args:ArrayList<Any>) : Action()
}

sealed class ActionUiModel {

    class GetAllPropertyModel(val listProperty: List<Property>) : ActionUiModel()

    class Error(val message: String?) : ActionUiModel()

    class Loading(val isLoading: Boolean) : ActionUiModel()

    class GetPropertyModel(val property: Property) : ActionUiModel()

    class GetPropertyBySearchModel(val listProperty: List<Property>) : ActionUiModel()
}