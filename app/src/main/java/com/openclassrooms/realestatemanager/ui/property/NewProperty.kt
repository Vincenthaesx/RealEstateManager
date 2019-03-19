package com.openclassrooms.realestatemanager.ui.property

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiActivity
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import com.openclassrooms.realestatemanager.models.Property
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.row_new_property.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class NewProperty : BaseUiActivity<Action, ActionUiModel, PropertyTranslator>() {

    private var type: String = ""
    private var address: String = ""
    private var price: String = ""
    private var surface: Int = 0
    private var roomsCount: Int = 0
    private var bathroomsCount: Int = 0
    private var bedroomsCount: Int = 0
    private var description: String = ""
    private lateinit var pictureList: List<String>
    private var status: Boolean = true
    private lateinit var entryDate : Date
    private var saleDate: Date? = null
    private var agent: String = ""


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

        type = edtType.text.toString()

        address = edtAddress.text.toString()

        price = edtAddress.text.toString()

        surface = edtSurface.text.length

        roomsCount = edtRoomCount.text.length

        description = edtDescription.text.toString()

        bedroomsCount = edtBedroomsCount.text.length

        bathroomsCount = edtBathroomsCount.text.length

        status = true

        saleDate = null

        agent = edtAgent.text.toString()

        val property = Property(0, type, address, price, surface, roomsCount, bathroomsCount, bedroomsCount, description, pictureList, status, entryDate, saleDate, agent   )

        Toast.makeText(this, "pictureList: $pictureList", Toast.LENGTH_LONG).show()

        if (type.isNotEmpty() && address.isNotEmpty() && price.isNotEmpty() && surface != 0 && roomsCount != 0 && bathroomsCount != 0 && bedroomsCount != 0 && description.isNotEmpty() && pictureList.isNotEmpty() && agent.isNotEmpty()  ) {
        actions.onNext(Action.AddNewProperty(property))
        }
        else {
            Toast.makeText(this, "Please enter all the input fields", Toast.LENGTH_LONG).show()
        }
    }

    // ----------

    private fun disposeWhenDestroy() {
        this.disposable.clear()
    }

    private fun configureButton() {
        imgButtonSelect.setOnClickListener {
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
