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
import com.openclassrooms.realestatemanager.utils.GlideApp
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.log
import com.wbinarytree.github.kotlinutilsrecyclerview.GenericAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_property_detail.*
import kotlinx.android.synthetic.main.fragment_property_detail_item.*
import kotlinx.android.synthetic.main.row_image_detail.*

class PropertyDetailFragment : BaseUiFragment<Action, ActionUiModel, PropertyTranslator>(){

    private var idProperty: Int = 0

    override fun render(ui: ActionUiModel) {
        when(ui) {
            is ActionUiModel.GetPropertyModel -> {

                recyclerView_detailImage.adapter = GenericAdapter(R.layout.row_image_detail, ui.property.pictureList) { image, _ ->

                    GlideApp.with(this@PropertyDetailFragment)
                            .load(image)
                            .fitCenter()
                            .override ( 300 , 300 )
                            .into(imageRecyclerView)
                }

                when {
                    resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT -> {

                    }
                    resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE -> {
                        GlideApp.with(this)
                                .load(START_URL+ui.property.address+ END_URL)
                                .fitCenter()
                                .override ( 300 , 300 )
                                .into(image_map)
                    }
                    else -> Log.e("TAG", "Error")
                }

                descriptionDetail.text = ui.property.description

                surface.text = ui.property.surface

                numberOfRoom.text = ui.property.roomsCount.toString()

                numberOfBathrooms.text = ui.property.bathroomsCount.toString()

                numberOfBedrooms.text = ui.property.bedroomsCount.toString()

                location.text = ui.property.address

            }
            is ActionUiModel.Error -> {
                ui.message?.log()
            }
        }
    }

    override fun translator(): PropertyTranslator = requireActivity().getViewModel()

    override fun getLayout() = R.layout.fragment_property_detail

    private val disposable : CompositeDisposable by lazy {
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
            intent.putExtra(ID_PROPERTY, idProperty )
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
        private const val START_URL = "https://maps.googleapis.com/maps/api/staticmap?center="
        private const val END_URL = "&zoom=19&size=600x400&maptype=roadmap&${Utils.API_KEY}"
    }
}
