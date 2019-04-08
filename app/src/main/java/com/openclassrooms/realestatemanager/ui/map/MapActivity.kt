package com.openclassrooms.realestatemanager.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.Marker
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateManagerApplication
import com.openclassrooms.realestatemanager.ui.base.BaseUiActivity
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.map_fragment.*
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

class MapActivity : BaseUiActivity<Action, ActionUiModel, MapTranslator>(), LocationListener {

    override fun translator(): MapTranslator = getViewModel()

    private lateinit var mMapView: MapView

    private var mGoogleApiClient: GoogleApiClient? = null
    private var googleMap: GoogleMap? = null
    private var disposable: Disposable? = null
    private var mLocationRequest: LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_fragment)

        mMapView = mapView
        this.configureMapView()
        this.configureLocationRequest()

        mMapView.onCreate(savedInstanceState)
        mMapView.onResume()
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        }
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
        this.disposeWhenDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()

    }

    private fun disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable!!.isDisposed) this.disposable!!.dispose()
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private fun checkLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(Objects.requireNonNull<Context>(this), *perms)
    }

    private fun configureLocationRequest() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval((100 * 1000).toLong())        // 100 seconds, in milliseconds
                .setFastestInterval(1000) // 1 second, in milliseconds
    }

    override fun onLocationChanged(location: Location?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(ui: ActionUiModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun configureMapView() {
        try {
            MapsInitializer.initialize(RealEstateManagerApplication.applicationContext())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (checkLocationPermission()) {
            googleMap?.isMyLocationEnabled = true
        }


        mMapView.getMapAsync { mMap ->
            googleMap = mMap
            if (checkLocationPermission()) {
                //Request location updates:
                googleMap!!.isMyLocationEnabled = true
            }
            googleMap!!.uiSettings.isCompassEnabled = true
            googleMap!!.uiSettings.isMyLocationButtonEnabled = true
            val locationButton = (mMapView.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2"))

            // and next place it, for example, on bottom right (as Google Maps app)
            val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            rlp.setMargins(0, 0, 30, 30)
            googleMap!!.uiSettings.isRotateGesturesEnabled = true
            googleMap!!.setOnMarkerClickListener { this.onClickMarker(it) }
        }
    }

    private fun onClickMarker(marker: Marker): Boolean {
        return if (marker.tag != null) {
            Log.e("TAG", "onClickMarker: " + marker.tag!!)
            true
        } else {
            Log.e("TAG", "onClickMarker: ERROR NO TAG")
            false
        }
    }

    companion object {

        const val API_KEY = Utils.API_KEY
        private val perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    }
}