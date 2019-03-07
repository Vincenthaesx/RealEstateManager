package com.openclassrooms.realestatemanager.ui.main

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.utils.safeCast
import com.openclassrooms.realestatemanager.view.PropertyAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_property.*

class PropertyFragment : PropertyAdapter.Listener, BaseUiFragment<Action, ActionUiModel, MainTranslator>(){


    override fun render(ui: ActionUiModel) {
        when(ui) {
            is ActionUiModel.GetAllPropertyModel -> {
                updateUI(ui.listProperty)
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

        actions.onNext(Action.GetAllProperty())

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
        if(fragment_property_recyclerView.adapter  == null){
            fragment_property_recyclerView.adapter = PropertyAdapter(mutableListOf())
        }
    }

    private fun configureSwipeRefreshLayout() {
        fragment_property_swipeRefresh.setOnRefreshListener { actions.onNext(Action.GetAllProperty()) }
    }


    private fun disposeWhenDestroy() {
        this.disposable.clear()
    }


    // -------------------
    // UPDATE UI
    // -------------------

    private fun updateUI(res: List<Property>) {

        fragment_property_recyclerView.adapter?.safeCast<PropertyAdapter> {
            it.updateData(res)
        }
        fragment_property_swipeRefresh.isRefreshing = false
    }
}
