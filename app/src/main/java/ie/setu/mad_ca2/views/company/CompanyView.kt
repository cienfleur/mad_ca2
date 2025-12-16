package ie.setu.mad_ca2.views.company

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import ie.setu.mad_ca2.R
import ie.setu.mad_ca2.databinding.ActivityMainBinding // Assuming your layout is activity_main.xml
import ie.setu.mad_ca2.models.Company
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CompanyView : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: CompanyPresenter
    private var companyTimestamp: Long = 0L

    // The launcher is a UI component, so it belongs in the View.
    lateinit var imagePickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = CompanyPresenter(this)

        // Register the UI callbacks
        registerImagePickerCallback()
        // registerMapCallback() // If you move this from the presenter, it goes here too.

        setupSpinner()
        setupDatePicker()

        // Use the ImageView to trigger the image selection
        binding.companyImage.setOnClickListener {
            presenter.doSelectImage()
        }

        binding.btnAdd.setOnClickListener {
            presenter.doAddCompany(
                binding.companyName.text.toString(),
                binding.companyDescription.text.toString(),
                binding.companyCountry.selectedItem.toString(),
                companyTimestamp
            )
        }
    }

    // This function handles the result of the image picker.
    private fun registerImagePickerCallback() {
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    // This is crucial for displaying local images after an app restart
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    // Pass the selected URI to the presenter to handle the logic
                    presenter.doImageSelected(uri)
                }
            }
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            this, R.array.countries, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.companyCountry.adapter = adapter
        }
    }

    private fun setupDatePicker() {
        binding.companyDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedCalendar = Calendar.getInstance().apply {
                        set(selectedYear, selectedMonth, selectedDay)
                    }
                    companyTimestamp = selectedCalendar.timeInMillis
                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.companyDate.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }

    // Called by the Presenter to update the UI
    fun showCompany(company: Company) {
        binding.companyName.setText(company.name)
        binding.companyDescription.setText(company.description)
        if (company.date != 0L) {
            companyTimestamp = company.date
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.companyDate.setText(format.format(company.date))
        }
        val countriesArray = resources.getStringArray(R.array.countries)
        val countryIndex = countriesArray.indexOf(company.country)
        if (countryIndex >= 0) {
            binding.companyCountry.setSelection(countryIndex)
        }
        binding.btnAdd.setText(R.string.save_company)
        updateImage(Uri.parse(company.image)) // Use the new updateImage function
    }

    // A single function to update the ImageView, called by Presenter
    fun updateImage(imageUriString: Uri) {
        if (imageUriString.toString().isNotEmpty()) {
            Picasso.get()
                .load(imageUriString)
                .resize(200, 200)
                .into(binding.companyImage)
        }
    }
}
