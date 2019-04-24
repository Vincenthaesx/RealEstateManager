package com.openclassrooms.realestatemanager.ui.property

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.ui.newProperty.NewProperty
import com.openclassrooms.realestatemanager.utils.GlideApp
import com.openclassrooms.realestatemanager.utils.doubleToStringNoDecimal
import com.openclassrooms.realestatemanager.utils.log
import com.wbinarytree.github.kotlinutilsrecyclerview.GenericAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_property.*
import kotlinx.android.synthetic.main.fragment_property_item.*

class PropertyFragment : BaseUiFragment<Action, ActionUiModel, PropertyTranslator>() {

    private lateinit var propertyDetailFragment: PropertyDetailFragment

    override fun render(ui: ActionUiModel) {

        when (ui) {
            is ActionUiModel.GetAllPropertyModel -> {
                fragment_property_recyclerView.adapter = GenericAdapter(R.layout.fragment_property_item, ui.listProperty) { property, _ ->

                    GlideApp.with(this@PropertyFragment)
                            .load(property.pictureList.first())
                            .fitCenter()
                            .override(300, 300)
                            .into(image_property)

                    property_type.text = property.type
                    property_city.text = property.address

                    val price = property.price
                    val priceProperty = doubleToStringNoDecimal(price)

                    property_price.text = (priceProperty + "€")

                    if (property.status == "Sold") {
                        image_property_sold.visibility = View.VISIBLE
                    }

                    itemView.setOnClickListener {
                        if (resources.getBoolean(R.bool.isTab)) {
                            propertyDetailFragment = PropertyDetailFragment()

                            val bundle = Bundle()
                            bundle.putInt("id", property.pid)
                            propertyDetailFragment.arguments = bundle

                            fragmentManager?.beginTransaction()
                                    ?.replace(R.id.activity_main_frame_propertyDetail, propertyDetailFragment)
                                    ?.commit()

                        } else {
                            propertyDetailFragment = PropertyDetailFragment()

                            val bundle = Bundle()
                            bundle.putInt("id", property.pid)
                            propertyDetailFragment.arguments = bundle

                            fragmentManager?.beginTransaction()
                                    ?.replace(R.id.activity_main_frame_property, propertyDetailFragment)
                                    ?.addToBackStack(null)
                                    ?.commit()
                        }
                    }
                }
            }
            is ActionUiModel.GetPropertyBySearchModel -> {
                fragment_property_recyclerView.adapter = GenericAdapter(R.layout.fragment_property_item, ui.listProperty) { property, _ ->

                    GlideApp.with(this@PropertyFragment)
                            .load(property.pictureList.first())
                            .fitCenter()
                            .override(300, 300)
                            .into(image_property)

                    property_type.text = property.type
                    property_city.text = property.address

                    val price = property.price
                    val priceProperty = doubleToStringNoDecimal(price)

                    property_price.text = (priceProperty + "€")

                    if (property.status == "Sold") {
                        image_property_sold.visibility = View.VISIBLE
                    }

                    itemView.setOnClickListener {
                        propertyDetailFragment = PropertyDetailFragment()

                        val bundle = Bundle()
                        bundle.putInt("id", property.pid)
                        propertyDetailFragment.arguments = bundle

                        fragmentManager?.beginTransaction()
                                ?.replace(R.id.frameLayout_search, propertyDetailFragment)
                                ?.addToBackStack(null)
                                ?.commit()
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

    override fun translator(): PropertyTranslator = requireActivity().getViewModel()

    override fun getLayout() = R.layout.fragment_property

    private val disposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureView()
        configureSwipeRefreshLayout()

        btnAddNewProperty.setOnClickListener {
            val intent = Intent(activity, NewProperty::class.java)
            startActivity(intent)

        }
    }

    override fun onDestroy() {
        this.disposeWhenDestroy()
        super.onDestroy()
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private fun configureView() {
        fragment_property_recyclerView.layoutManager = LinearLayoutManager(activity)

        val bundle = arguments?.size()
        if (bundle != null) {
            val query = arguments?.getString("QUERY")
            val args = arguments?.getStringArrayList("ARGS") as ArrayList<Any>

            query?.let { Action.GetPropertyBySearch(it, args) }?.let { actions.onNext(it) }

            btnAddNewProperty.visibility = View.GONE
        } else {
            actions.onNext(Action.GetAllProperty())
        }
    }

    private fun configureSwipeRefreshLayout() {
        fragment_property_swipeRefresh.setOnRefreshListener { actions.onNext(Action.GetAllProperty()) }
    }

    private fun disposeWhenDestroy() {
        this.disposable.clear()
    }

}
