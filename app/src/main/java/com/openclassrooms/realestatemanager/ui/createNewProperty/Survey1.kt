package com.openclassrooms.realestatemanager.ui.createNewProperty

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.row_new_property.*
import java.text.SimpleDateFormat
import java.util.*

class Survey1 : Fragment() {

    private lateinit var entryDate : Date
    private lateinit var survey2: Survey2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_survey1, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureButton()

        nextProperty.setOnClickListener {

            if (edtType.text.isNotEmpty() && edtAddress.text.isNotEmpty() && edtPrice.text.isNotEmpty() && edtSurface.text.isNotEmpty() && edtDescription.text.isNotEmpty() && edtAgent.text.isNotEmpty() && entryDate.time.toString().isNotEmpty()) {
                val type = edtType.text.toString()
                val address = edtAddress.text.toString()
                val price = edtPrice.text.toString()
                val surface = edtSurface.text.toString()
                val description = edtDescription.text.toString()
                val agent = edtAgent.text.toString()

                survey2 = Survey2()
                val bundle = Bundle()
                bundle.putString(TYPE, type)
                bundle.putString(ADDRESS, address)
                bundle.putString(PRICE, price)
                bundle.putString(SURFACE, surface)
                bundle.putString(DESCRIPTION, description)
                bundle.putString(AGENT, agent)
                bundle.putString(DATE, entryDate.time.toString())

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

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureButton() {

        txtNumberDate.setOnClickListener {

            val textView: TextView = txtNumberDate
            textView.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

            val cal = Calendar.getInstance()

            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                textView.text = sdf.format(cal.time)
                entryDate = cal.time
            }

            DatePickerDialog(activity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }
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
