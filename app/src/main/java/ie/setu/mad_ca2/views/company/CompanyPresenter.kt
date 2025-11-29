package ie.setu.mad_ca2.views.company

import android.net.Uri
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import timber.log.Timber.i
import ie.setu.mad_ca2.models.Company
import ie.setu.mad_ca2.main.MainApp
import ie.setu.mad_ca2.views.company.CompanyView
import ie.setu.mad_ca2.views.companylist.CompanyListView

// Presenter for the Company View
class CompanyPresenter(val view: CompanyView) {

    var company = Company()
    var app: MainApp = view.application as MainApp
    var tempUri: Uri = Uri.EMPTY
    var edit = false

    init {
        // If editing an existing company, load its image
        if (view.intent.hasExtra("company_edit")) {
            company = view.intent.extras?.getParcelable("company_edit")!!
            if (company.image.isNotEmpty()) {
                // If you are using Uri string in model:
                tempUri = Uri.parse(company.image)
                view.showImage(tempUri)
            }
        }
    }

    fun doAddCompany(name: String, description: String, country: String, date: String) {
        val company = Company()
        company.name = name
        company.description = description
        company.country = country
        company.date = date
        // Save the Uri as a String (Temporary until Firebase Storage is added)
        company.image = tempUri.toString()

        app.companies.create(company)
        i("Company Created: $company")
        val intent = Intent(view, CompanyListView::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Clears back stack
        view.startActivity(intent)
        // view.finish() // replace when finished testing

    }

    fun doSelectImage() {
        view.imagePickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    fun doUpdateImage(uri: Uri) {
        tempUri = uri
        view.showImage(uri)
    }


    // NEW: Called when the user picks an image
    fun doSaveImage(uri: Uri) {
        tempUri = uri
        view.showImage(uri)
    }
}
