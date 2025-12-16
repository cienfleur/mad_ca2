package ie.setu.mad_ca2.views.company

import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import ie.setu.mad_ca2.main.MainApp
import ie.setu.mad_ca2.models.Company
import timber.log.Timber

class CompanyPresenter(val view: CompanyView) {

    var company = Company()
    var app: MainApp = view.application as MainApp
    var edit = false

    init {
        if (view.intent.hasExtra("company_edit")) {
            edit = true
            company = view.intent.extras?.getParcelable("company_edit")!!
            view.showCompany(company)
        }
    }

    fun doAddCompany(name: String, description: String, country: String, date: Long) {
        // validate that each field is not empty

        company.name = name
        company.description = description
        company.country = country
        company.date = date

        if (edit) {
            app.companies.update(company)
            Timber.i("Company Updated: $company")
        } else {
            app.companies.create(company)
            Timber.i("Company Created: $company")
        }
        view.finish()
    }

    // The presenter tells the view to launch its image picker
    fun doSelectImage() {
        val request = PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
            .build()
        // The view owns the launcher
        view.imagePickerLauncher.launch(request)
    }

    // This function is called from the view's callback when an image is successfully picked.
    fun doImageSelected(uri: Uri) {
        // Update the model with the new image URI string
        company.image = uri.toString()
        Timber.i("Image Selected: ${company.image}")
        // Tell the view to update the UI with the new image
        view.updateImage(Uri.parse(company.image))
    }

    // Map logic can remain here for now, but should ideally be moved to the View as well
    // ... (registerMapCallback and other map functions)
}
