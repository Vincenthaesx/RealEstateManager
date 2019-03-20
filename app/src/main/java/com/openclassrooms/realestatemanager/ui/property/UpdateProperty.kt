package com.openclassrooms.realestatemanager.ui.property

import android.os.Bundle
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiActivity
import com.openclassrooms.realestatemanager.ui.base.getViewModel

class UpdateProperty  : BaseUiActivity<Action, ActionUiModel, PropertyTranslator>()  {

    private var id: Int = 0

    override fun render(ui: ActionUiModel) {
        when(ui){
            is ActionUiModel.GetPropertyModel -> {

            }
        }
    }

    override fun translator(): PropertyTranslator = getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_property)
        id = intent.getIntExtra(ID_PROPERTY, 0)

        actions.onNext(Action.GetProperty(id))
    }

    companion object {
        private const val ID_PROPERTY = "idProperty"
    }
}
