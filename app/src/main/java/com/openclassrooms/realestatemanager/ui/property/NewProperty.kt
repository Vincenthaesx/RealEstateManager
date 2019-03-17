package com.openclassrooms.realestatemanager.ui.property

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.base.BaseUiActivity
import com.openclassrooms.realestatemanager.ui.base.getViewModel
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.row_new_property.*
import java.text.SimpleDateFormat
import java.util.*

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

        validateNewProperty.setOnClickListener {
            retrieveParameterForProperty()
        }

        btnCalendar.setOnClickListener {

            val textView: TextView = findViewById(R.id.edtEntryDate)
            textView.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

            var cal = Calendar.getInstance()


            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                textView.text = sdf.format(cal.time)

            }

            DatePickerDialog(this, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        configureToolBar()
    }

    override fun onDestroy() {
        disposeWhenDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

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

        edtType.text

        imgButtonSelect.setOnClickListener {
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
