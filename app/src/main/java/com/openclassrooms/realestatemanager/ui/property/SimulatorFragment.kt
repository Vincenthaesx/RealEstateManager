package com.openclassrooms.realestatemanager.ui.property

import android.os.Bundle
import android.view.View
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import kotlinx.android.synthetic.main.fragment_property_detail_item.*
import kotlinx.android.synthetic.main.fragment_simulator.*
import kotlinx.android.synthetic.main.fragment_simulator.price
import java.util.regex.Pattern

class SimulatorFragment : BaseUiFragment<Action, ActionUiModel, PropertyTranslator>() {

    private var idProperty: Int = 0

    override fun render(ui: ActionUiModel) {
        when (ui) {
            is ActionUiModel.GetPropertyModel -> {

                var price1 = ui.property.price
                var rate = 2
                var duration = 0
                var apport = 0

                val p = Pattern.compile("\\d+\\s\\d+")
                val m = p.matcher(price1)
                while (m.find()) {
                    price.text = m.group()
                    price1 = m.group()
                }

                btnSimulatorResult.setOnClickListener {
                    apport = edtApport.text.toString().toInt()
                    duration = edtDuration.text.toString().toInt()

                    val total = ((price1.toInt() - apport) *2/100 )
                    val priceByMouth = ((price1.toInt() - apport) *2/100) / 12 * duration

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
