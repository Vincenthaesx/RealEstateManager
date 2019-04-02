package com.openclassrooms.realestatemanager.ui.updateProperty

import com.openclassrooms.realestatemanager.models.Property

sealed class Action {

    class GetProperty(val id: Int) : Action()

    class GetPropertyForUpdate(val property: Property): Action()

}

sealed class ActionUiModel {

    class Error(val message: String?) : ActionUiModel()

    class GetPropertyModel(val property: Property) : ActionUiModel()

}