package ie.setu.mad_ca2.views.company

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.squareup.picasso.Picasso
import ie.setu.mad_ca2.R
import ie.setu.mad_ca2.databinding.ActivityMainBinding
import ie.setu.mad_ca2.models.Company
import java.util.Calendar

class CompanyView : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: CompanyPresenter

    lateinit var imagePickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = CompanyPresenter(this)

        registerImagePicker()
        setupSpinner()
        setupDatePicker()

        binding.btnChooseImage.setOnClickListener {
            presenter.doSelectImage()
        }

        binding.btnAdd.setOnClickListener {
            presenter.doAddCompany(
                binding.companyName.text.toString(),
                binding.companyDescription.text.toString(),
                binding.companyCountry.selectedItem.toString(),
                binding.companyDate.text.toString()
            )
        }
    }

    private fun registerImagePicker() {
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                presenter.doUpdateImage(uri)
            }
        }
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.countries,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.companyCountry.adapter = adapter
        }
    }

    private fun setupDatePicker() {
        binding.companyDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.companyDate.setText(selectedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }
    fun showCompany(company: Company) {
        binding.companyName.setText(company.name)
        binding.companyDescription.setText(company.description)
        binding.companyDate.setText(company.date)
        binding.btnAdd.setText(R.string.save_company) // Update button text
        if (company.image.isNotEmpty()) {
            Picasso.get().load(Uri.parse(company.image)).into(binding.companyImage)
        }
    }

    fun showImage(uri: Uri) {
        Picasso.get().load(uri).into(binding.companyImage)
    }

}
