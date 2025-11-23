package ie.setu.mad_ca2.views.company

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ie.setu.mad_ca2.R
import ie.setu.mad_ca2.models.Company
import java.util.Calendar

class CompanyView : AppCompatActivity() {

    lateinit var presenter: CompanyPresenter
    private lateinit var companyName: EditText
    private lateinit var companyDescription: EditText
    private lateinit var companyCountry: Spinner
    private lateinit var companyDate: EditText
    private lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = CompanyPresenter(this)

        companyName = findViewById(R.id.companyName)
        companyDescription = findViewById(R.id.companyDescription)
        companyCountry = findViewById(R.id.companyCountry)
        companyDate = findViewById(R.id.companyDate)
        btnAdd = findViewById(R.id.btnAdd)

        setupSpinner()
        setupDatePicker()

        btnAdd.setOnClickListener {
            presenter.doAddCompany(
                companyName.text.toString(),
                companyDescription.text.toString(),
                companyCountry.selectedItem.toString(),
                companyDate.text.toString()
            )
        }
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.countries,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            companyCountry.adapter = adapter
        }
    }

    private fun setupDatePicker() {
        companyDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    companyDate.setText(selectedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }
}
