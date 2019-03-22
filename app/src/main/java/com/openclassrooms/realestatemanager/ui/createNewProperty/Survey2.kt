package com.openclassrooms.realestatemanager.ui.createNewProperty

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

class Survey2 : BaseUiFragment<Action, ActionUiModel, NewPropertyTranslator>() {

    override fun render(ui: ActionUiModel) {
        when (ui){
            is ActionUiModel.AddNewPropertyModel -> {
                Toast.makeText(activity, "New property added with id = ${ui.success}", Toast.LENGTH_LONG).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
            if(requestCode == REQUEST_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {

                val uri: Uri? = intent?.data

                recyclerViewNewProperty.adapter = GenericAdapter(R.layout.row_image_detail, listOf(uri)) { image , _ ->

                    GlideApp.with(this@Survey2)
                            .load(image)
                            .fitCenter()
                            .override ( 300 , 300 )
                            .into(imageRecyclerView)
                }

            }
            else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {

                val imageBitmap = intent?.extras?.get("intent") as Bitmap
                val storage = saveToInternalStorage(imageBitmap)

                recyclerViewNewProperty.adapter = GenericAdapter(R.layout.row_image_detail, listOf(storage)) { image , _ ->

                    GlideApp.with(this@Survey2)
                            .load(image)
                            .fitCenter()
                            .override ( 300 , 300 )
                            .into(imageRecyclerView)
                }

                pictureList = listOf(storage)

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

        val property = Property(0, type, address, price, surface, roomsCount, bathroomsCount, bedroomsCount, description, pictureList, status, date , saleDate, agent   )

        if (roomsCount.toString().isNotEmpty() && bathroomsCount.toString().isNotEmpty() && bedroomsCount.toString().isNotEmpty() && pictureList.isNotEmpty()) {
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
