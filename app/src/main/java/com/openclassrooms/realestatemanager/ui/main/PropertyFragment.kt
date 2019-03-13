package com.openclassrooms.realestatemanager.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.utils.log
import com.squareup.picasso.Picasso
import com.wbinarytree.github.kotlinutilsrecyclerview.GenericAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_property.*
import kotlinx.android.synthetic.main.fragment_property_item.*

class PropertyFragment : BaseUiFragment<Action, ActionUiModel, MainTranslator>(){

    private lateinit var propertyDetailFragment: PropertyDetailFragment

    override fun render(ui: ActionUiModel) {
        when(ui) {
            is ActionUiModel.GetAllPropertyModel -> {
                fragment_property_recyclerView.adapter = GenericAdapter(R.layout.fragment_property_item, ui.listProperty) { property, _ ->

                    Picasso.get()
                            .load(property.pictureList.first())
                            .fit()
                            .into(image_property)

                    property_type.text = property.type

                    property_city.text = property.address

                    property_price.text = property.price

                    when {
                        resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT -> itemView.setOnClickListener {
                            propertyDetailFragment = PropertyDetailFragment()
                            val bundle = Bundle()
                            bundle.putInt("id", property.pid)

                            propertyDetailFragment.arguments = bundle
                            fragmentManager?.beginTransaction()
                                    ?.add(R.id.activity_main_frame_property, propertyDetailFragment)
                                    ?.addToBackStack(null)
                                    ?.commit()
                        }
                        resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE -> itemView.setOnClickListener {
                            propertyDetailFragment = PropertyDetailFragment()
                            val bundle = Bundle()
                            bundle.putInt("id", property.pid)

                            propertyDetailFragment.arguments = bundle
                            fragmentManager?.beginTransaction()
                                    ?.add(R.id.activity_main_frame_propertyDetail, propertyDetailFragment)
                                    ?.addToBackStack(null)
                                    ?.commit()
                        }
                        else -> Log.e("TAG", "Error")
                    }
                   }
                }
            is ActionUiModel.Error -> {
                ui.message?.log()
                fragment_property_swipeRefresh.isRefreshing = false
                }
            is ActionUiModel.Loading -> {
                fragment_property_swipeRefresh.isRefreshing = ui.isLoading
                }
            }
        }

    override fun translator(): MainTranslator = requireActivity().getViewModel()

    override fun getLayout() = R.layout.fragment_property

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
        fragment_property_recyclerView.layoutManager = LinearLayoutManager(activity)
        actions.onNext(Action.GetAllProperty())
    }

    private fun configureSwipeRefreshLayout() {
        fragment_property_swipeRefresh.setOnRefreshListener { actions.onNext(Action.GetAllProperty()) }
    }

    private fun disposeWhenDestroy() {
        this.disposable.clear()
    }

}
