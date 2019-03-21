package com.openclassrooms.realestatemanager.ui.createNewProperty

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.openclassrooms.realestatemanager.RealEstateManagerApplication
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import kotlinx.android.synthetic.main.row_new_property1.*
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.Format
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Survey2 : BaseUiFragment<Action, ActionUiModel, NewPropertyTranslator>() {

    override fun render(ui: ActionUiModel) {
        when (ui){
            is ActionUiModel.AddNewPropertyModel -> {

            }
        }
    }

    private var type: String = ""
    private var address: String = ""
    private var price: String = ""
    private var surface: String = ""
    private var description: String = ""
    private var agent: String = ""
    private var entryDate: String = ""
    private lateinit var date: Date

    override fun translator(): NewPropertyTranslator = requireActivity().getViewModel()

    override fun getLayout() = com.openclassrooms.realestatemanager.R.layout.fragment_survey2

    private lateinit var pictureList: List<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        takePicture()

        val bundle = this.arguments

        if (bundle != null) {
            type = bundle.getString(TYPE)
            address = bundle.getString(ADDRESS)
            price = bundle.getString(PRICE)
            surface = bundle.getString(SURFACE)
            description = bundle.getString(DESCRIPTION)
            agent = bundle.getString(AGENT)
            entryDate = bundle.getString(DATE)
        }


        validateNewProperty.setOnClickListener {
            retrieveParameterForProperty()
        }
    }


    private fun dispatchTakePictureIntent() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(RealEstateManagerApplication.applicationContext(), android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(RealEstateManagerApplication.applicationContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(RealEstateManagerApplication.applicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    // --------------------
    // SAVE SOME PROPERTY
    // --------------------

    private fun takePicture() {
        imgButtonSelect.setOnClickListener {
            checkPermissions()
            dispatchTakePictureIntent()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val storage = saveToInternalStorage(imageBitmap)

            pictureList = listOf(storage)
        }
    }

    private fun retrieveParameterForProperty() {

        val roomsCount = edtRoomCount.text.toString().toInt()
        val bedroomsCount = edtBedroomsCount.text.toString().toInt()
        val bathroomsCount = edtBathroomsCount.text.toString().toInt()
        val status = true
        val saleDate = null

        val property = Property(0, type, address, price, surface, roomsCount, bathroomsCount, bedroomsCount, description, pictureList, status, date , saleDate, agent   )

        if (roomsCount.toString().isNotEmpty() && bathroomsCount.toString().isNotEmpty() && bedroomsCount.toString().isNotEmpty() && pictureList.isNotEmpty()) {
            actions.onNext(Action.AddNewProperty(property))
        }
        else {
            Toast.makeText(activity, "Please enter all the input fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): String {
        val rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val time = System.currentTimeMillis()
        val myPath = File(rootPath, "$time.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(myPath)
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fos?.close()
        }
        return myPath.absolutePath
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 0
        private const val TYPE = "type"
        private const val ADDRESS = "address"
        private const val PRICE = "price"
        private const val SURFACE = "surface"
        private const val DESCRIPTION = "description"
        private const val AGENT = "agent"
        private const val DATE = "date"

    }

}
