package com.openclassrooms.realestatemanager.ui.updateProperty

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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.ui.base.BaseUiFragment
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import com.openclassrooms.realestatemanager.ui.main.MainActivity
import com.openclassrooms.realestatemanager.utils.GlideApp
import com.openclassrooms.realestatemanager.utils.log
import com.wbinarytree.github.kotlinutilsrecyclerview.GenericAdapter
import kotlinx.android.synthetic.main.alert_label_edit_text.view.*
import kotlinx.android.synthetic.main.row_image_detail.*
import kotlinx.android.synthetic.main.row_update_property.*
import java.io.File
import java.io.FileOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FragmentUpdateSurvey2 : BaseUiFragment<Action, ActionUiModel, UpdatePropertyTranslator>() {

    override fun render(ui: ActionUiModel) {
        when (ui) {
            is ActionUiModel.GetPropertyModel -> {
                pictureList.addAll(ui.property.pictureList)
                listDescriptionImage.addAll(ui.property.descriptionPictureList)

                setupAdapter()

                edtPointOfInterest.setText(ui.property.PointOfInterest)
                edtRoomCount.setText(ui.property.roomsCount.toString())
                edtBathroomsCount.setText(ui.property.bathroomsCount.toString())
                edtBedroomsCount.setText(ui.property.bedroomsCount.toString())
            }
            is ActionUiModel.Error -> {
                ui.message?.log()
            }
        }
    }

    private var idProperty: Int = 0
    private lateinit var type: String
    private lateinit var address: String
    private var price: Int = 0
    private var surface: Int = 0
    private lateinit var description: String
    private lateinit var agent: String
    private lateinit var entryDate: String
    private var soldDate: Date? = null
    private lateinit var date: Date
    private val datetimePicker: SingleDateAndTimePickerDialog by lazy {
        SingleDateAndTimePickerDialog.Builder(context)
                .displayMinutes(false)
                .displayHours(false)
                .mainColor(resources.getColor(R.color.blue_01))
                .minDateRange(Date())
                .title(resources.getString(R.string.label_date))
                .curved()
                .minutesStep(1)
                .listener { date ->
                    val textView: TextView = txtNumberDateUpdate
                    textView.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())
                    val myFormat = "dd-MM-yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)

                    soldDate = date
                    textView.text = sdf.format(date.time)
                }
                .build()
    }

    private var listDescriptionImage: MutableList<String> = mutableListOf()
    private val pictureList: MutableList<String> = mutableListOf()

    override fun translator(): UpdatePropertyTranslator = requireActivity().getViewModel()

    override fun getLayout() = R.layout.fragment_survey_update


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        takePicture()
        txtClickOnPicture.text = getString(R.string.messageForUpdatePicture)
        txtClickOnPicture.visibility = View.VISIBLE

        val bundle = this.arguments

        if (bundle != null) {
            type = bundle.getString(TYPE)
            address = bundle.getString(ADDRESS)
            price = bundle.getInt(PRICE)
            surface = bundle.getInt(SURFACE)
            description = bundle.getString(DESCRIPTION)
            agent = bundle.getString(AGENT)
            entryDate = bundle.getString(DATE)
            idProperty = bundle.getInt(ID_PROPERTY)
        }

        txtNumberDateUpdate.setOnClickListener {
            if (!datetimePicker.isDisplaying) {
                datetimePicker.display()
            }
        }

        configureRecyclerView()

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

    private fun startImagePickIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    // --------------------
    // SAVE SOME PROPERTY
    // --------------------

    private fun takePicture() {
        imgButtonSelect.setOnClickListener {
            val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
            builder?.setTitle(R.string.locationPicture)
            builder?.setItems(arrayOf(getString(R.string.phoneStorage), getString(R.string.camera)), (DialogInterface.OnClickListener { _, i ->
                when (i) {
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
        if (requestCode == REQUEST_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            val uri: Uri? = data?.data
            pictureList.add(uri.toString())
            setupAdapter()
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val storage = saveToInternalStorage(imageBitmap)
            pictureList.add(storage)
            setupAdapter()
        } else {
            Toast.makeText(activity, "Echec request !", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupAdapter() {
        if (recyclerViewNewProperty.adapter != null) {
            recyclerViewNewProperty.adapter?.notifyItemInserted(pictureList.size - 1)
        } else {
            recyclerViewNewProperty.adapter = GenericAdapter(R.layout.row_image_detail, pictureList) { image, position ->

                GlideApp.with(this@FragmentUpdateSurvey2)
                        .load(image)
                        .centerCrop()
                        .override(300, 300)
                        .into(imageRecyclerView)

                imageRecyclerView.setOnClickListener {
                    configureAlertDialog(position)
                }

                val s = listDescriptionImage.getOrNull(position)
                if (s != null) {
                    txtImageRecyclerView.text = s
                    txtImageRecyclerView.visibility = View.VISIBLE
                } else {
                    txtImageRecyclerView.visibility = View.GONE
                }
            }

        }
    }

    private fun configureAlertDialog(position: Int) {
        val dialogBuilder = this.context?.let { AlertDialog.Builder(it) }

        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_label_edit_text, null)
        dialogBuilder?.setView(dialogView)
        dialogBuilder?.setTitle(getString(R.string.descriptionpicture))

        dialogBuilder?.setPositiveButton(getString(R.string.yes)) { _, _ ->

            if (listDescriptionImage.isEmpty()) {
                listDescriptionImage.add(dialogView.edtRecyclerViewImage.text.toString())
            } else {
                if (listDescriptionImage.size <= position) {
                    listDescriptionImage.add(position, dialogView.edtRecyclerViewImage.text.toString())
                } else {
                    listDescriptionImage[position] = dialogView.edtRecyclerViewImage.text.toString()
                }
            }
            recyclerViewNewProperty.adapter?.notifyItemChanged(position)
        }
        dialogBuilder?.setNegativeButton(getString(R.string.no)) { _, _ ->
        }

        val alertDialog = dialogBuilder?.create()
        alertDialog?.show()

    }

    private fun retrieveParameterForProperty() {

        var roomsCount = 0
        var bedroomsCount = 0
        var bathroomsCount = 0
        var pointOfInterest = ""

        if (!edtRoomCount.text.isNullOrEmpty()) {
            roomsCount = edtRoomCount.text.toString().toInt()
        } else {
            Toast.makeText(activity, R.string.input, Toast.LENGTH_SHORT).show()
        }

        if (!edtBedroomsCount.text.isNullOrEmpty()) {
            bedroomsCount = edtBedroomsCount.text.toString().toInt()
        } else {
            Toast.makeText(activity, R.string.input, Toast.LENGTH_SHORT).show()
        }

        if (!edtBathroomsCount.text.isNullOrEmpty()) {
            bathroomsCount = edtBathroomsCount.text.toString().toInt()
        } else {
            Toast.makeText(activity, R.string.input, Toast.LENGTH_SHORT).show()
        }

        if (!edtPointOfInterest.text.isNullOrEmpty()) {
            pointOfInterest = edtPointOfInterest.text.toString()
        } else {
            Toast.makeText(activity, R.string.input, Toast.LENGTH_SHORT).show()
        }
        var status = "Available"

        if (soldDate != null) {
            status = "Sold"
        }

        val property = Property(idProperty, type, address, price, surface, roomsCount, bathroomsCount, bedroomsCount, description, pointOfInterest, pictureList, listDescriptionImage, status, date, soldDate, agent)

        if (pictureList.isNotEmpty() && listDescriptionImage.isNotEmpty() && pictureList.size == listDescriptionImage.size && roomsCount != 0 && bedroomsCount != 0 && bathroomsCount != 0 && pointOfInterest.isNotEmpty()) {
            actions.onNext(Action.GetPropertyForUpdate(property))
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(activity, R.string.input, Toast.LENGTH_SHORT).show()
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
        actions.onNext(Action.GetProperty(idProperty))
    }

    companion object {
        private const val ID_PROPERTY = "idProperty"
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