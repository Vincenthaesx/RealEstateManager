package com.openclassrooms.realestatemanager.ui.property

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateManagerApplication
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : BaseUiFragment<Action, ActionUiModel, PropertyTranslator>(), LocationListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var myMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private lateinit var pairOfProperty: Pair<List<String>, List<Int>>

    override fun render(ui: ActionUiModel) {
        when (ui) {
            is ActionUiModel.GetAllPropertyModel -> {
                val locationAddress = ui.listProperty.map { it.address }
                val idProperty = ui.listProperty.map { it.pid }

                pairOfProperty = Pair(locationAddress, idProperty)
            }
        }
    }

    override fun translator(): PropertyTranslator = requireActivity().getViewModel()

    override fun getLayout() = R.layout.fragment_map

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        actions.onNext(Action.GetAllProperty())

        if (Utils.isInternetAvailable(RealEstateManagerApplication.applicationContext())){
            val supportMapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
            supportMapFragment?.getMapAsync(this)
        }else{
            Toast.makeText(context,"Connexion error",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap

        myMap.uiSettings.isRotateGesturesEnabled = true

        myMap.setOnMarkerClickListener(this)

        setUpMap()
    }

    private fun getLocationFromAddress(context: Context, strAddress: String): List<LatLng?> {
        val coder = Geocoder(context)
        val address: List<Address>?

        try {
            address = coder.getFromLocationName(strAddress, 3)
            if (address == null) {
                return emptyList()
            }

            return address.map {
                LatLng(it.latitude, it.longitude)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(context!!,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
            return
        }

        myMap.isMyLocationEnabled = true
        myMap.uiSettings.isMyLocationButtonEnabled = false

        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                progressBarMap.visibility = View.GONE
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                val options = MarkerOptions()

                val address = pairOfProperty.first.map {
                    getLocationFromAddress(context!!, it).firstOrNull()
                }

                for (point in address) {
                    myMap.addMarker(point.let { it?.let { it1 -> options.position(it1) } }).apply {
                        val id = address.indexOf(point)
                        tag = pairOfProperty.second[id]
                    }
                }

                imgMyLocation.setOnClickListener {
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        return if (marker?.tag != null) {
            launchDetailFragment(marker.tag as Int)
            true
        } else {
            Log.e("TAG", "onClickMarker: ERROR NO TAG")
            false
        }
    }

    private fun launchDetailFragment(databaseId: Int) {
        val propertyDetailFragment = PropertyDetailFragment()

        val bundle = Bundle()
        bundle.putInt(ID, databaseId)
        propertyDetailFragment.arguments = bundle

        fragmentManager?.beginTransaction()
                ?.replace(R.id.activity_main_frame_property, propertyDetailFragment)
                ?.addToBackStack(null)
                ?.commit()
    }


    override fun onLocationChanged(location: Location) {

    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

    companion object {
        private const val ID = "id"
        private const val PERMISSION_REQUEST_CODE = 110
    }
}