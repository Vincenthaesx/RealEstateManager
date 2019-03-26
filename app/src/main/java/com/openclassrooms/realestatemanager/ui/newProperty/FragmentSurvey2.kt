package com.openclassrooms.realestatemanager.ui.newProperty

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.ui.main.MainActivity
import com.openclassrooms.realestatemanager.utils.GlideApp
import com.wbinarytree.github.kotlinutilsrecyclerview.GenericAdapter
import kotlinx.android.synthetic.main.row_image_detail.*
import kotlinx.android.synthetic.main.row_new_property1.*
import java.io.File
import java.io.FileOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FragmentSurvey2 : BaseUiFragment<Action, ActionUiModel, NewPropertyTranslator>() {

    override fun render(ui: ActionUiModel) {
        when (ui){
            is ActionUiModel.AddNewPropertyModel -> {
                view?.let {
                    Snackbar.make(it, "New property added with id = ${ui.success}", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                }
            }
        }
    }

    private lateinit var type: String
    private lateinit var address: String
    private lateinit var price: String
    private lateinit var surface: String
    private lateinit var description: String
    private lateinit var agent: String
    private lateinit var entryDate: String
    private lateinit var date: Date
    private var listDescriptionImage: MutableList<String> = mutableListOf()

    override fun translator(): NewPropertyTranslator = requireActivity().getViewModel()

    override fun getLayout() = com.openclassrooms.realestatemanager.R.layout.fragment_survey2

    private val pictureList: MutableList<String> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        takePicture()
        configureRecyclerView()

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

        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        try {
            date = dateFormat.parse(entryDate)
        } catch (e: ParseException) {
            e.printStackTrace()
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

    private fun startImagePickIntent(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    // --------------------
    // SAVE SOME PROPERTY
    // --------------------

    private fun takePicture() {
        imgButtonSelect.setOnClickListener {
//            checkPermissions()
            val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
            builder?.setTitle("Picture location")
            builder?.setItems(arrayOf("On phone storage", "Take picture with camera"), (DialogInterface.OnClickListener { _, i ->
                when(i){
                    // Phone
                    0 -> {
                        if (context?.let { it1 -> ContextCompat.checkSelfPermission(it1, Manifest.permission.READ_EXTERNAL_STORAGE) }
                                != PackageManager.PERMISSION_GRANTED) {
                            // Permission is not granted
                            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            } else {
                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions(activity!!,
                                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                        REQUEST_READ_EXTERNAL_STORAGE)
                            }
                        } else {
                            startImagePickIntent()
                        }
                    }
                    // Camera
                    1 -> {
                        if (context?.let { it1 -> ContextCompat.checkSelfPermission(it1, Manifest.permission.WRITE_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            activity?.let { it1 ->
                                ActivityCompat.requestPermissions(it1,
                                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                                        REQUEST_IMAGE_CAPTURE)
                            }
                        } else {
                            dispatchTakePictureIntent()
                        }
                    }
                }
            }))

            builder?.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if(requestCode == REQUEST_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {

                val uri: Uri? = data?.data
                pictureList.add(uri.toString())

                recyclerViewNewProperty.adapter = GenericAdapter(R.layout.row_image_detail, pictureList) { image, _ ->

                    GlideApp.with(this@FragmentSurvey2)
                            .load(image)
                            .centerCrop()
                            .override ( 300 , 300 )
                            .into(imageRecyclerView)
                }
            }
            else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {

                val imageBitmap = data?.extras?.get("data") as Bitmap
                val storage = saveToInternalStorage(imageBitmap)
                pictureList.add(storage)

                recyclerViewNewProperty.adapter = GenericAdapter(R.layout.row_image_detail, pictureList) { image, _ ->

                    GlideApp.with(this@FragmentSurvey2)
                            .load(image)
                            .centerCrop()
                            .override(300, 300)
                            .into(imageRecyclerView)
                }
            } else {
                Toast.makeText(activity, "Echec request !", Toast.LENGTH_LONG).show()
            }
    }

    private fun retrieveParameterForProperty() {

        val roomsCount = edtRoomCount.text.toString().toInt()
        val bedroomsCount = edtBedroomsCount.text.toString().toInt()
        val bathroomsCount = edtBathroomsCount.text.toString().toInt()
        val status = true
        val saleDate = null

        listDescriptionImage.addAll(listOf(edtImageRecyclerView.text.toString()))

        val property = Property(0, type, address, price, surface, roomsCount, bathroomsCount, bedroomsCount, description, pictureList, listDescriptionImage, status, date , saleDate, agent   )

        if (roomsCount != 0 && bathroomsCount != 0 && bedroomsCount != 0 && pictureList.isNotEmpty() && listDescriptionImage.isNotEmpty() && pictureList.size == listDescriptionImage.size) {
            actions.onNext(Action.AddNewProperty(property))
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
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

    // -----------------
    // CONFIGURATION
    // -----------------

    private fun configureRecyclerView() {
        recyclerViewNewProperty.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 0
        private const val REQUEST_READ_EXTERNAL_STORAGE = 7
        private const val REQUEST_IMAGE = 9
        private const val TYPE = "type"
        private const val ADDRESS = "address"
        private const val PRICE = "price"
        private const val SURFACE = "surface"
        private const val DESCRIPTION = "description"
        private const val AGENT = "agent"
        private const val DATE = "date"
    }
}
