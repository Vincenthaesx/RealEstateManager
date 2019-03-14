package com.openclassrooms.realestatemanager.ui.property

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiActivity
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import kotlinx.android.synthetic.main.activity_new_property.*
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable

class NewProperty : BaseUiActivity<Action, ActionUiModel, PropertyTranslator>() {

    private val disposable : CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun render(ui: ActionUiModel) {
        when(ui){
            is ActionUiModel.AddNewPropertyModel -> {
                Toast.makeText(this, "success with id = ${ui.success}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun translator(): PropertyTranslator = getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_property)

        buttonAddNewProperty.setOnClickListener {
            retrieveParameterForProperty()
        }

        configureToolBar()
    }

    override fun onDestroy() {
        disposeWhenDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            testImageView.setImageBitmap(imageBitmap)
        }
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.newPropertyToolbar)
        setSupportActionBar(toolbar)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun retrieveParameterForProperty() {
        button_add_picture.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    // ----------

    private fun disposeWhenDestroy() {
        this.disposable.clear()
    }




    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 0
    }

}
