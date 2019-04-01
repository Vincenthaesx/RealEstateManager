package com.openclassrooms.realestatemanager.ui.newProperty

import com.openclassrooms.realestatemanager.models.Property

sealed class Action {

    class AddNewProperty(val newProperty: Property) : Action()

}

sealed class ActionUiModel {

    class AddNewPropertyModel(val success: Long) : ActionUiModel()

    class Error(val message: String?) : ActionUiModel()

}