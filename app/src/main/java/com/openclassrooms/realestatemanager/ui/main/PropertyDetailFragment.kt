package com.openclassrooms.realestatemanager.ui.main

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.utils.log
import com.squareup.picasso.Picasso
import com.wbinarytree.github.kotlinutilsrecyclerview.GenericAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_property_detail_item.*
import kotlinx.android.synthetic.main.fragment_property_detail_item.view.*
import kotlinx.android.synthetic.main.row_image_detail.*

class PropertyDetailFragment : BaseUiFragment<Action, ActionUiModel, MainTranslator>(){

    private var idProperty: Int = 0

    override fun render(ui: ActionUiModel) {
        when(ui) {
            is ActionUiModel.GetPropertyModel -> {

                recyclerView_detailImage.adapter = GenericAdapter(R.layout.row_image_detail, ui.property.pictureList) { image, _ ->
                    Picasso.get()
                            .load(image)
                            .fit()
                            .into(imageRecyclerView)
                }

                Picasso.get()
                        .load(ui.property.pictureList.first())
                        .fit()
                        .into(image_map)

                descriptionDetail.text = ui.property.description

                surface.text = ui.property.surface.toString()

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

    override fun translator(): MainTranslator = requireActivity().getViewModel()

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

        configureRecyclerView()


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

    private fun disposeWhenDestroy() {
        this.disposable.clear()
    }

}
