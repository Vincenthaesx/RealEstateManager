package com.openclassrooms.realestatemanager.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
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
import com.openclassrooms.realestatemanager.ui.base.BaseUiActivity
import com.openclassrooms.realestatemanager.ui.base.getViewModel

class MapActivity : BaseUiActivity<Action, ActionUiModel, MapTranslator>(), LocationListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var myMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private lateinit var locationAddress: List<String>
    private lateinit var idProperty: List<Int>

    override fun render(ui: ActionUiModel) {
        when (ui) {
            is ActionUiModel.GetAllPropertyModel -> {
                locationAddress = ui.listProperty.map { it.address }
                idProperty = ui.listProperty.map { it.pid }
            }
        }
    }

    override fun translator(): MapTranslator = getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_fragment)

        requestPermission()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        actions.onNext(Action.GetAllProperty())

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSION_REQUEST_CODE)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap

        myMap.setOnMarkerClickListener(this)

        setUpMap()
    }

    private fun getLocationFromAddress(context: Context, strAddress: String): List<LatLng?> {
        val coder = Geocoder(context)
        val address: List<Address>?

        try {
            address = coder.getFromLocationName(strAddress, 3)
            if (address == null) {
                return listOf(null)
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
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
            return
        }

        myMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                val options = MarkerOptions()

                val address = locationAddress.map {
                    getLocationFromAddress(this, it).firstOrNull()
                }

                for (point in address) {
                    myMap.addMarker(point?.let { options.position(it) }).apply {
                        tag = idProperty
                    }
                }

                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        return if (marker?.tag != null) {
            Log.e("TAG", "onClickMarker: " + marker.tag!!)
            true
        } else {
            Log.e("TAG", "onClickMarker: ERROR NO TAG")
            false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if (grantResults.size > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
                    mapFragment?.getMapAsync(this)

                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show()
                }
            }
        }
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
        private const val PERMISSION_REQUEST_CODE: Int = 110
    }
}