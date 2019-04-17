package com.openclassrooms.realestatemanager.ui.property

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.ui.updateProperty.UpdateProperty
import com.openclassrooms.realestatemanager.utils.GlideApp
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.doubleToStringNoDecimal
import com.openclassrooms.realestatemanager.utils.log
import com.wbinarytree.github.kotlinutilsrecyclerview.GenericAdapter
import kotlinx.android.synthetic.main.fragment_property_detail.*
import kotlinx.android.synthetic.main.fragment_property_detail_item.*
import kotlinx.android.synthetic.main.row_image_detail.*
import java.text.SimpleDateFormat
import java.util.*

class PropertyDetailFragment : BaseUiFragment<Action, ActionUiModel, PropertyTranslator>() {

    private var idProperty: Int = 0
    private var locationAddress: String = ""

    override fun render(ui: ActionUiModel) {
        when (ui) {
            is ActionUiModel.GetPropertyModel -> {

                recyclerView_detailImage.adapter = GenericAdapter(R.layout.row_image_detail, ui.property.pictureList) { image, description ->

                    GlideApp.with(this@PropertyDetailFragment)
                            .load(image)
                            .centerCrop()
                            .override(300, 300)
                            .into(imageRecyclerView)

                    if (image.isNotEmpty()) {
                        txtImageRecyclerView.visibility = View.VISIBLE
                        txtImageRecyclerView.text = ui.property.descriptionPictureList[description]
                    }
                }

                if (ui.property.status) {
                    imgButtonEdit.visibility = View.VISIBLE
                } else {
                    soldDate.visibility = View.VISIBLE
                    txtSold.visibility = View.VISIBLE
                    val myFormat = "dd-MM-yyyy"
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    soldDate.text = sdf.format(ui.property.saleDate?.time)
                }

                descriptionDetail.text = ui.property.description

                surface.text = ui.property.surface

                numberOfRoom.text = ui.property.roomsCount.toString()

                numberOfBathrooms.text = ui.property.bathroomsCount.toString()

                val myFormat = "dd-MM-yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                entryDate.text = sdf.format(ui.property.entryDate.time)

                val priceP = ui.property.price
                val priceProperty = doubleToStringNoDecimal(priceP)

                price.text = (priceProperty+"€")

                numberOfBedrooms.text = ui.property.bedroomsCount.toString()

                agent.text = ui.property.agent

                pointOfInterest.text = ui.property.PointOfInterest

                location.text = ui.property.address

                locationAddress = ui.property.address

                val latLng = context?.let { getLocationFromAddress(it, locationAddress) }

                GlideApp.with(this)
                        .load("$START_URL${latLng?.latitude},${latLng?.longitude}&size=600x400&${Utils.API_KEY}")
                        .fitCenter()
                        .override(300, 300)
                        .into(imgMap)

                btnSimulator.setOnClickListener {
                    val simulatorFragment = SimulatorFragment()
                    val bundle = Bundle()
                    bundle.putInt(ID_SIMULATOR, idProperty)
                    simulatorFragment.arguments = bundle

                    fragmentManager?.beginTransaction()
                            ?.replace(R.id.activity_main_frame_property, simulatorFragment)
                            ?.addToBackStack(null)
                            ?.commit()
                }

            }
            is ActionUiModel.Error -> {
                ui.message?.log()
            }
            is ActionUiModel.Loading -> {
                fragment_property_detail_refresh.isRefreshing = ui.isLoading
            }
        }
    }

    override fun translator(): PropertyTranslator = requireActivity().getViewModel()

    override fun getLayout() = R.layout.fragment_property_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments

        if (bundle != null) {
            idProperty = bundle.getInt("id", idProperty)
        }

        configureRecyclerView()
        configureSwipeRefreshLayout()

        imgButtonEdit.setOnClickListener {
            val intent = Intent(activity, UpdateProperty::class.java)
            intent.putExtra(ID_PROPERTY, idProperty)
            startActivity(intent)
        }
    }

    private fun getLocationFromAddress(context: Context, strAddress: String): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: LatLng? = null

        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location = address[0]
            location.latitude
            location.longitude

            p1 = LatLng(location.latitude, location.longitude)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return p1

    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private fun configureRecyclerView() {
        recyclerView_detailImage.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        actions.onNext(Action.GetProperty(idProperty))
    }

    private fun configureSwipeRefreshLayout() {
        fragment_property_detail_refresh.setOnRefreshListener { actions.onNext(Action.GetProperty(idProperty)) }
    }

    companion object {

        private const val ID_PROPERTY = "idProperty"
        private const val ID_SIMULATOR = "idSimulator"
        private const val START_URL = "https://maps.googleapis.com/maps/api/staticmap?zoom=19&markers="
    }
}
