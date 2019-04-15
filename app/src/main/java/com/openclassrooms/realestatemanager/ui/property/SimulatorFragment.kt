package com.openclassrooms.realestatemanager.ui.property

import android.os.Bundle
import android.view.View
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel

class SimulatorFragment : BaseUiFragment<Action, ActionUiModel, PropertyTranslator>()  {

    private var idProperty: Int = 0

    override fun render(ui: ActionUiModel) {
        when (ui) {
            is ActionUiModel.GetPropertyModel -> {


            }
        }
    }

    override fun translator(): PropertyTranslator = requireActivity().getViewModel()

    override fun getLayout() = R.layout.fragment_simulator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments

        if (bundle != null) {
            idProperty = bundle.getInt(ID_SIMULATOR, idProperty)
        }

        actions.onNext(Action.GetProperty(idProperty))

    }

    companion object {
        private const val ID_SIMULATOR = "idSimulator"
    }
}
