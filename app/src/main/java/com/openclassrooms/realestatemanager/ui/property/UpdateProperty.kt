package com.openclassrooms.realestatemanager.ui.property

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiActivity
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.utils.GlideApp
import com.wbinarytree.github.kotlinutilsrecyclerview.GenericAdapter
import kotlinx.android.synthetic.main.activity_update_property.*
import kotlinx.android.synthetic.main.row_image_detail.*

class UpdateProperty  : BaseUiActivity<Action, ActionUiModel, PropertyTranslator>()  {

    private var id: Int = 0

    override fun render(ui: ActionUiModel) {
        when(ui) {
            is ActionUiModel.GetPropertyModel -> {
                recyclerView_update_property_image.adapter = GenericAdapter(R.layout.row_image_detail, ui.property.pictureList) { image, _ ->

                    GlideApp.with(this@UpdateProperty)
                            .load(image)
                            .fitCenter()
                            .override ( 300 , 300 )
                            .into(imageRecyclerView)
                }

            }
        }
    }

    override fun translator(): PropertyTranslator = getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_property)
        id = intent.getIntExtra(ID_PROPERTY, 0)

        configureRecyclerView()
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private fun configureRecyclerView() {
        recyclerView_update_property_image.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        actions.onNext(Action.GetProperty(id))
    }

    companion object {
        private const val ID_PROPERTY = "idProperty"
    }
}
