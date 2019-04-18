package com.openclassrooms.realestatemanager.ui.property

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.utils.toFRDate
import kotlinx.android.synthetic.main.fragment_search.*
import java.text.SimpleDateFormat
import java.util.*

class SearchFragment : BaseUiFragment<Action, ActionUiModel, PropertyTranslator>() {

    override fun render(ui: ActionUiModel) {
        when (ui) {
            is ActionUiModel.GetPropertyBySearchModel -> {
                // some result
            }
        }
    }

    override fun translator(): PropertyTranslator = requireActivity().getViewModel()

    override fun getLayout() = R.layout.fragment_search

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        search_fragment_spinner.setOnClickListener { this.displayPopupMenu(resources.getStringArray(R.array.search_type_array), search_fragment_spinner) }
        search_fragment_statute.setOnClickListener { this.displayPopupMenu(resources.getStringArray(R.array.search_statute_array), search_fragment_statute) }
        search_fragment_from_date.setOnClickListener { this.displayDatePicker(search_fragment_from_date) }
        search_fragment_to_date.setOnClickListener { this.displayDatePicker(search_fragment_to_date) }
        search_fragment_FAB.setOnClickListener { this.buildSearchQuery() }
    }

    private fun displayPopupMenu(listToDisplay: Array<String>, view: TextInputEditText) {
        val popupMenu = PopupMenu(this.context, view)
        (0 until listToDisplay.size).forEach { it ->
            popupMenu.menu.add(Menu.NONE, it, it, listToDisplay[it])
            popupMenu.setOnMenuItemClickListener { view.setText(it.title);true }
        }
        popupMenu.show()
    }

    private fun displayDatePicker(mView: TextInputEditText) {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val calendar: Calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this.context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            mView.setText(sdf.format(Date(year - 1900, monthOfYear, dayOfMonth)), TextView.BufferType.EDITABLE)

        }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private fun buildSearchQuery() {
        val estateType = search_fragment_spinner.text.toString()
        val estateStatute = search_fragment_statute.text.toString()
        val estatePriceMin = search_fragment_price_min.text.toString().toDoubleOrNull()
        val estatePriceMax = search_fragment_price_max.text.toString().toDoubleOrNull()
        val estateSurfaceMin = search_fragment_surface_min.text.toString().toIntOrNull()
        val estateSurfaceMax = search_fragment_surface_max.text.toString().toIntOrNull()
        val estateBedroomMin = search_fragment_bedrooms_min.text.toString().toIntOrNull()
        val estateBedroomMax = search_fragment_bedrooms_max.text.toString().toIntOrNull()
        val estateRoomMin = search_fragment_room_min.text.toString().toIntOrNull()
        val estateRoomMax = search_fragment_room_max.text.toString().toIntOrNull()
        val estateBathroomMin = search_fragment_bathrooms_min.text.toString().toIntOrNull()
        val estateBathroomMax = search_bathrooms_room_max.text.toString().toIntOrNull()
        val estateFromDate = try {
            search_fragment_from_date.text.toString().toFRDate()
        } catch (e: Exception) {
            null
        }
        val estateToDate = try {
            search_fragment_to_date.text.toString().toFRDate()
        } catch (e: Exception) {
            null
        }
        val estatePhoto = search_fragment_media_min.text.toString().toIntOrNull() ?: 0

        var query = "Select * FROM Property"
        val args = arrayListOf<Any>()
        var conditions = false

        if (!(estateType == "" || estateType == "ALL")) {
            query += " WHERE type = :$estateType"
            args.add(estateType)
            conditions = true
        }

        if (!(estateStatute == "" || estateStatute == "ALL")) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "status = :$estateStatute"
            args.add(estateStatute)
        }

        if (estatePriceMin != null) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "price >= :${estatePriceMin.toInt()}"
            args.add(estatePriceMin.toInt())
        }

        if (estatePriceMax != null) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "price <= :${estatePriceMax.toInt()}"
            args.add(estatePriceMax.toInt())
        }

        if (estateSurfaceMin != null) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "surface >= :$estateSurfaceMin"
            args.add(estateSurfaceMin)
        }

        if (estateSurfaceMax != null) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "surface <= :$estateSurfaceMax"
            args.add(estateSurfaceMax)
        }

        if (estateBedroomMin != null) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "bedroomsCount >= :$estateBedroomMin"
            args.add(estateBedroomMin)
        }

        if (estateBedroomMax != null) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "bedroomsCount <= :$estateBedroomMax"
            args.add(estateBedroomMax)
        }

        if (estateRoomMin != null) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "roomsCount >= :$estateBedroomMin"
            args.add(estateRoomMin)
        }

        if (estateRoomMax != null) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "roomsCount <= :$estateBedroomMax"
            args.add(estateRoomMax)
        }

        if (estateBathroomMin != null) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "bathroomsCount >= :$estateBedroomMin"
            args.add(estateBathroomMin)
        }

        if (estateBathroomMax != null) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "bathroomsCount <= :$estateBedroomMax"
            args.add(estateBathroomMax)
        }

        if (estateFromDate != null) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "entryDate >= ?"
            args.add(estateFromDate.time)
        }

        if (estateToDate != null) {
            query += if (conditions) " AND " else " WHERE "
            query += "entryDate <= ?"
            args.add(estateToDate.time)
        }

        query += " AND pictureList >= ?"
        args.add(estatePhoto)

        launchListFragment(query, args)

    }

    private fun launchListFragment(query: String, args: ArrayList<Any>) {
        val bundle = Bundle()
        val propertyFragment = PropertyFragment()
        bundle.putString("QUERY", query)
        bundle.putStringArrayList("ARGS", args as ArrayList<String>)
        propertyFragment.arguments = bundle

        fragmentManager?.beginTransaction()
                ?.replace(R.id.activity_main_frame_property, propertyFragment)
                ?.addToBackStack(null)
                ?.commit()
    }
}
