package com.openclassrooms.realestatemanager.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Property

class PropertyAdapter(
        private val propertyList: MutableList<Property>) : androidx.recyclerview.widget.RecyclerView.Adapter<PropertyViewHolder>() {

    interface Listener

    // Create view holder and inflating its xml layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_property_item, parent, false)

        return PropertyViewHolder(view)
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    override fun onBindViewHolder(viewHolder: PropertyViewHolder, position: Int) {
        viewHolder.updateWithResult(propertyList[position])
    }

    fun updateData(propertyList : List<Property>){
        this.propertyList.clear()
        this.propertyList.addAll(propertyList)
        notifyDataSetChanged()
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return this.propertyList.size
    }

    // Returns a specific result in the results list
    fun getProperty(position: Int): Property {
        return this.propertyList[position]
    }

}

class PropertyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    fun updateWithResult(applications: Property) {



    }

}
