package com.openclassrooms.realestatemanager.ui.property

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.utils.log
import kotlinx.android.synthetic.main.fragment_survey1.*
import kotlinx.android.synthetic.main.row_new_property.*
import java.text.SimpleDateFormat
import java.util.*

class FragmentUpdateSurvey1 : BaseUiFragment<Action, ActionUiModel, PropertyTranslator>() {

    override fun render(ui: ActionUiModel) {
        when (ui) {
            is ActionUiModel.GetPropertyModel -> {

                edtType.hint = ui.property.type
                edtDescription.hint = ui.property.description
                edtSurface.hint = ui.property.surface
                edtAddress.hint = ui.property.address
                edtPrice.hint = ui.property.price
                txtNumberDate.text = ui.property.entryDate.toString()
                edtAgent.hint = ui.property.agent

            }
            is ActionUiModel.Error -> {
                ui.message?.log()
            }
        }
    }

    override fun translator(): PropertyTranslator = requireActivity().getViewModel()

    override fun getLayout() = R.layout.fragment_survey1

    var idProperty: Int = 0
    private var entryDate: String = ""
    private var survey2: FragmentUpdateSurvey2 = FragmentUpdateSurvey2()
    private val datetimePicker: SingleDateAndTimePickerDialog by lazy {
        SingleDateAndTimePickerDialog.Builder(context)
                .displayMinutes(false)
                .displayHours(false)
                .mainColor(resources.getColor(R.color.blue_01))
//                .minDateRange(Date())                                     // for fix selected date to today date or after.
                .title(resources.getString(R.string.label_date))
                .curved()
                .minutesStep(1)
                .listener { date ->
                    val textView: TextView = txtNumberDate
                    textView.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())
                    val myFormat = "dd-MM-yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)

                    entryDate = sdf.format(date.time)
                    textView.text = sdf.format(date.time)
                }
                .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments

        if (bundle != null) {
            idProperty = bundle.getInt(ID_PROPERTY_BUNDLE, idProperty)
        }
        actions.onNext(Action.GetProperty(idProperty))

        txtNumberDate.setOnClickListener {
            if (!datetimePicker.isDisplaying) {
                datetimePicker.display()
            }
        }

        nextProperty.setOnClickListener {

            if (edtType.text.isNotEmpty() && edtAddress.text.isNotEmpty() && edtPrice.text.isNotEmpty() && edtSurface.text.isNotEmpty() && edtDescription.text.isNotEmpty() && edtAgent.text.isNotEmpty() && entryDate.isNotEmpty()) {
                val type = edtType.text.toString()
                val address = edtAddress.text.toString()
                val price = edtPrice.text.toString()
                val surface = edtSurface.text.toString()
                val description = edtDescription.text.toString()
                val agent = edtAgent.text.toString()

                val bundle = Bundle()
                bundle.putString(TYPE, type)
                bundle.putString(ADDRESS, address)
                bundle.putString(PRICE, price)
                bundle.putString(SURFACE, surface)
                bundle.putString(DESCRIPTION, description)
                bundle.putString(AGENT, agent)
                bundle.putString(DATE, entryDate)
                bundle.putInt(ID_PROPERTY, idProperty)

                survey2.arguments = bundle

                fragmentManager?.beginTransaction()
                        ?.replace(R.id.frameLayout_updateProperty, survey2)
                        ?.addToBackStack(null)
                        ?.commit()
            } else {
                Toast.makeText(activity, "Please enter all the input fields", Toast.LENGTH_LONG).show()
            }

        }
    }

    companion object {
        private const val ID_PROPERTY = "idProperty"
        private const val ID_PROPERTY_BUNDLE = "propertyId"
        private const val TYPE = "type"
        private const val ADDRESS = "address"
        private const val PRICE = "price"
        private const val SURFACE = "surface"
        private const val DESCRIPTION = "description"
        private const val AGENT = "agent"
        private const val DATE = "date"
    }

}