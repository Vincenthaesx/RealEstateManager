package com.openclassrooms.realestatemanager.ui.property

import android.os.Bundle
import android.view.View
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import kotlinx.android.synthetic.main.row_simulator.*

class SimulatorFragment : BaseUiFragment<Action, ActionUiModel, PropertyTranslator>() {

    private var idProperty: Int = 0

    override fun render(ui: ActionUiModel) {
        when (ui) {
            is ActionUiModel.GetPropertyModel -> {
                var bring = 0
                var duration = 1

                val priceProperty = ui.property.price
                price.text = "$priceProperty €"
                edtDuration.setText(duration.toString())
                edtApport.setText(bring.toString())

                btnSimulatorResult.setOnClickListener {

                    duration = edtDuration.text.toString().toInt()
                    bring = edtApport.text.toString().toInt()


                    val total = ((priceProperty - bring) + ((2 * priceProperty) / 100)).toFloat()
                    val priceByMouth = ((priceProperty - bring) + ((2 * priceProperty) / 100)) / (12 * duration).toFloat()

                    constraintResult.visibility = View.VISIBLE

                    resultTotal.text = "$total €"
                    resultByMouth.text = "$priceByMouth €"
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
