package com.openclassrooms.realestatemanager.ui.main

import com.openclassrooms.realestatemanager.models.Property

sealed class Action {

    class GetAllProperty(): Action()

}


sealed class ActionUiModel {

    class GetAllPropertyModel(val listProperty: List<Property>): ActionUiModel()

    class Error(val message: String?) : ActionUiModel()
}