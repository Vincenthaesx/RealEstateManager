package com.openclassrooms.realestatemanager.ui.property

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiActivity
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.openclassrooms.realestatemanager.models.Property
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_new_property.*
import kotlinx.android.synthetic.main.row_new_property.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class NewProperty : BaseUiActivity<Action, ActionUiModel, PropertyTranslator>() {

    private lateinit var pictureList: List<String>
    private lateinit var entryDate : Date

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

        validateNewProperty.setOnClickListener {
            retrieveParameterForProperty()
        }

        configureButton()
        configureToolBar()
    }

    override fun onDestroy() {
        disposeWhenDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val storage = saveToInternalStorage(imageBitmap)

            pictureList = listOf(storage)
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

    private fun checkPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    // --------------------
    // SAVE SOME PROPERTY
    // --------------------

    private fun saveToInternalStorage(bitmapImage: Bitmap): String {
        val rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val time = System.currentTimeMillis()
        val mypath = File(rootPath, "$time.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fos?.close()
        }
        return mypath.absolutePath
    }


    private fun retrieveParameterForProperty() {

        val type = edtType.text.toString()
        val address = edtAddress.text.toString()
        val price = edtAddress.text.toString()
        val surface = edtSurface.text.toString().toInt()
        val roomsCount = edtRoomCount.text.toString().toInt()
        val bedroomsCount = edtBedroomsCount.text.toString().toInt()
        val bathroomsCount = edtBathroomsCount.text.toString().toInt()
        val description = edtDescription.text.toString()
        val status = true
        val saleDate = null
        val agent = edtAgent.text.toString()

        val property = Property(0, type, address, price, surface, roomsCount, bathroomsCount, bedroomsCount, description, pictureList, status, entryDate, saleDate, agent   )

        if (type.isNotEmpty() && address.isNotEmpty() && price.isNotEmpty() && surface != 0 && roomsCount != 0 && bathroomsCount != 0 && bedroomsCount != 0 && description.isNotEmpty() && pictureList.isNotEmpty() && agent.isNotEmpty()  ) {
          actions.onNext(Action.AddNewProperty(property))
        }
        else {
            Toast.makeText(this, "Please enter all the input fields", Toast.LENGTH_LONG).show()
        }
    }

    // ---------------

    private fun disposeWhenDestroy() {
        this.disposable.clear()
    }

    // ---------------
    // CONFIGURATION
    // ---------------

    private fun configureButton() {
        imgButtonSelect.setOnClickListener {
            checkPermissions()
            dispatchTakePictureIntent()
        }

        txtNumberDate.setOnClickListener {

            val textView: TextView = findViewById(R.id.txtNumberDate)
            textView.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

            val cal = Calendar.getInstance()

            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                textView.text = sdf.format(cal.time)
                entryDate = cal.time
            }

            DatePickerDialog(this, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 0
    }

}
