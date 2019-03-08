package com.openclassrooms.realestatemanager.ui.main

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.utils.log
import com.wbinarytree.github.kotlinutilsrecyclerview.GenericAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_property_detail.*
import kotlinx.android.synthetic.main.fragment_property_item.*

class PropertyDetailFragment : BaseUiFragment<Action, ActionUiModel, MainTranslator>(){

    override fun render(ui: ActionUiModel) {
        when(ui) {
            is ActionUiModel.GetAllPropertyModel -> {
                fragment_property_detail_recyclerView.adapter = GenericAdapter(R.layout.fragment_property_detail_item, ui.listProperty) { property, _ ->

                    property.let {
                        for (i in it.pictureList) {
                            Glide.with(itemView.context).load(i).into(image_property)
                        }
                    }

                    property_type.text = property.type

                    property_city.text = property.address

                    property_price.text = property.price

                }
            }
            is ActionUiModel.Error -> {
                ui.message?.log()
                fragment_property_detail_swipeRefresh.isRefreshing = false
            }
            is ActionUiModel.Loading -> {
                fragment_property_detail_swipeRefresh.isRefreshing = ui.isLoading
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
        fragment_property_detail_recyclerView.layoutManager = LinearLayoutManager(activity)
        actions.onNext(Action.GetAllProperty())
    }

    private fun configureSwipeRefreshLayout() {
        fragment_property_detail_swipeRefresh.setOnRefreshListener { actions.onNext(Action.GetAllProperty()) }
    }

    private fun disposeWhenDestroy() {
        this.disposable.clear()
    }

}
