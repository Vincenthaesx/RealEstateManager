package com.openclassrooms.realestatemanager.ui.property

import android.os.Bundle
import android.view.View
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import kotlinx.android.synthetic.main.fragment_simulator.*

class SimulatorFragment : BaseUiFragment<Action, ActionUiModel, PropertyTranslator>() {

    private var idProperty: Int = 0

    override fun render(ui: ActionUiModel) {
        when (ui) {
            is ActionUiModel.GetPropertyModel -> {

                val priceProperty = ui.property.price
                price.text = priceProperty.toString()

                btnSimulatorResult.setOnClickListener {
                    val apport = edtApport.text.toString().toInt()
                    val duration = edtDuration.text.toString().toInt()

                    val total = ((priceProperty - apport) + ((2*priceProperty)/100))
                    val priceByMouth = ((priceProperty - apport) + ((2*priceProperty)/100)) / (12 * duration)

                    resultTotal.visibility = View.VISIBLE
                    resultByMouth.visibility = View.VISIBLE
                    resultByMouthTitle.visibility = View.VISIBLE
                    resultTotalTitle.visibility = View.VISIBLE

                    resultTotal.text = total.toString()
                    resultByMouth.text = priceByMouth.toString()
                }
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
