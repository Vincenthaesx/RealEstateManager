package com.openclassrooms.realestatemanager.ui.newProperty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.Utils.onTouch
import kotlinx.android.synthetic.main.row_new_property.*
import java.text.SimpleDateFormat
import java.util.*

class FragmentSurvey1 : Fragment() {

    private var entryDate: String = ""
    private var survey2: FragmentSurvey2 = FragmentSurvey2()
    private val datetimePicker: SingleDateAndTimePickerDialog by lazy {
        SingleDateAndTimePickerDialog.Builder(context)
                .displayMinutes(false)
                .displayHours(false)
                .mainColor(resources.getColor(R.color.blue_01))
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_survey1, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTouch(edtDescription)
        onTouch(edtAddress)

        edtType.setOnClickListener { this.displayPopupMenu(resources.getStringArray(R.array.property_type_array), edtType) }

        txtNumberDate.setOnClickListener {

            if (!datetimePicker.isDisplaying) {
                datetimePicker.display()
            }
        }

        nextProperty.setOnClickListener {

            if (edtType.text!!.isNotEmpty() && edtAddress.text!!.isNotEmpty() && edtPrice.text!!.isNotEmpty() && edtSurface.text!!.isNotEmpty() && edtDescription.text!!.isNotEmpty() && edtAgent.text!!.isNotEmpty() && entryDate.isNotEmpty()) {
                val type = edtType.text.toString()
                val address = edtAddress.text.toString()
                val price = edtPrice.text.toString().toInt()
                val surface = edtSurface.text.toString().toInt()
                val description = edtDescription.text.toString()
                val agent = edtAgent.text.toString()

                val bundle = Bundle()
                bundle.putString(TYPE, type)
                bundle.putString(ADDRESS, address)
                bundle.putInt(PRICE, price)
                bundle.putInt(SURFACE, surface)
                bundle.putString(DESCRIPTION, description)
                bundle.putString(AGENT, agent)
                bundle.putString(DATE, entryDate)

                survey2.arguments = bundle

                fragmentManager?.beginTransaction()
                        ?.replace(R.id.frameLayout_newProperty, survey2)
                        ?.addToBackStack(null)
                        ?.commit()

            } else {
                Toast.makeText(activity, "Please enter all the input fields", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun displayPopupMenu(listToDisplay: Array<String>, view: TextInputEditText) {
        val popupMenu = PopupMenu(this.context, view)
        (0 until listToDisplay.size).forEach { it ->
            popupMenu.menu.add(Menu.NONE, it, it, listToDisplay[it])
            popupMenu.setOnMenuItemClickListener { view.setText(it.title);true }
        }
        popupMenu.show()
    }


    companion object {
        private const val TYPE = "type"
        private const val ADDRESS = "address"
        private const val PRICE = "price"
        private const val SURFACE = "surface"
        private const val DESCRIPTION = "description"
        private const val AGENT = "agent"
        private const val DATE = "date"
    }

}
