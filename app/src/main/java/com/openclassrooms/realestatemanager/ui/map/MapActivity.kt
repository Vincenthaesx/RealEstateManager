package com.openclassrooms.realestatemanager.ui.map

import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateManagerApplication
import com.openclassrooms.realestatemanager.ui.base.BaseUiActivity
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.map_fragment.*

class MapActivity : BaseUiActivity<Action, ActionUiModel, MapTranslator>() {

    override fun render(ui: ActionUiModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun translator(): MapTranslator = getViewModel()

    private lateinit var mMapView: MapView

    private var mGoogleApiClient: GoogleApiClient? = null
    private var disposable: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_fragment)

        mMapView = mapView

        mMapView.onCreate(savedInstanceState)
        mMapView.onResume()
        this.configureMapView()
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

    private fun configureMapView() {
        try {
            MapsInitializer.initialize(RealEstateManagerApplication.applicationContext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {

        const val API_KEY = Utils.API_KEY
    }
}