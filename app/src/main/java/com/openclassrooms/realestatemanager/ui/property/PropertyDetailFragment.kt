package com.openclassrooms.realestatemanager.ui.property

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.ui.updateProperty.UpdateProperty
import com.openclassrooms.realestatemanager.utils.GlideApp
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.log
import com.wbinarytree.github.kotlinutilsrecyclerview.GenericAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_property_detail.*
import kotlinx.android.synthetic.main.fragment_property_detail_item.*
import kotlinx.android.synthetic.main.row_image_detail.*

class PropertyDetailFragment : BaseUiFragment<Action, ActionUiModel, PropertyTranslator>() {

    private var idProperty: Int = 0
    private var markerLat: Double = 0.0
    private var markerLng: Double = 0.0
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

                descriptionDetail.text = ui.property.description

                surface.text = ui.property.surface

                numberOfRoom.text = ui.property.roomsCount.toString()

                numberOfBathrooms.text = ui.property.bathroomsCount.toString()

                numberOfBedrooms.text = ui.property.bedroomsCount.toString()

                location.text = ui.property.address

                locationAddress = ui.property.address


                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    actions.onNext(Action.GetGeocoding(locationAddress))
                }
            }
            is ActionUiModel.Error -> {
                ui.message?.log()
            }
            is ActionUiModel.Loading -> {
                fragment_property_detail_refresh.isRefreshing = ui.isLoading
            }
            is ActionUiModel.GetGeocodingModel -> {
                markerLat = ui.result.results?.get(0)?.geometry?.location?.lat!!
                markerLng = ui.result.results?.get(0)?.geometry?.location?.lng!!

                when {
                    resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT -> {

                    }
                    resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE -> {
                        GlideApp.with(this)
                                .load("$START_URL$markerLat,$markerLng&size=600x400&${Utils.API_KEY}")
                                .fitCenter()
                                .override(300, 300)
                                .into(imgMap)
                    }
                    else -> Log.e("TAG", "Error")
                }

            }
        }
    }

    override fun translator(): PropertyTranslator = requireActivity().getViewModel()

    override fun getLayout() = R.layout.fragment_property_detail

    private val disposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments

        if (bundle != null) {
            idProperty = bundle.getInt("id", idProperty)
        }

        imgButtonEdit.setOnClickListener {
            val intent = Intent(activity, UpdateProperty::class.java)
            intent.putExtra(ID_PROPERTY, idProperty)
            startActivity(intent)
        }

        configureRecyclerView()

        configureSwipeRefreshLayout()
    }

    override fun onDestroy() {
        this.disposeWhenDestroy()
        super.onDestroy()
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

    private fun disposeWhenDestroy() {
        this.disposable.clear()
    }

    companion object {

        private const val ID_PROPERTY = "idProperty"
        private const val START_URL = "https://maps.googleapis.com/maps/api/staticmap?zoom=19&markers="
    }
}
