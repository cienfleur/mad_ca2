package ie.setu.mad_ca2.views.company

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import ie.setu.mad_ca2.main.MainApp
import ie.setu.mad_ca2.models.Company
import ie.setu.mad_ca2.models.Location
import ie.setu.mad_ca2.views.editlocation.LocationView
import ie.setu.mad_ca2.views.map.MapView
import timber.log.Timber

class CompanyPresenter(val view: CompanyView) {

    var company = Company()
    var app: MainApp = view.application as MainApp
    var edit = false
    private var location = Location(52.245696, -7.139102, 15f)
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    init {
        if (view.intent.hasExtra("company_edit")) {
            edit = true
            company = view.intent.extras?.getParcelable("company_edit")!!
            if (company.lat != 0.0 && company.lng != 0.0) {
                location.lat = company.lat
                location.lng = company.lng
                location.zoom = company.zoom
            }
            view.showCompany(company)
            registerMapCallback()
        }
    }

    fun doAddCompany(name: String, description: String, country: String, date: Long) {
        company.name = name
        company.description = description
        company.country = country
        company.date = date
        // Save the location data to the company model before creating/updating
        company.lat = location.lat
        company.lng = location.lng
        company.zoom = location.zoom

        if (edit) {
            app.companies.update(company)
            Timber.i("Company Updated: $company")
        } else {
            app.companies.create(company)
            Timber.i("Company Created: $company")
        }
        view.finish()
    }
    fun doSelectImage() {
        val request = PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
            .build()
        view.imagePickerLauncher.launch(request)
    }

    fun doImageSelected(uri: Uri) {
        company.image = uri.toString()
        view.updateImage(Uri.parse(company.image))
    }

    fun doSetLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (company.zoom != 0f) {
            location.lat =  company.lat
            location.lng = company.lng
            location.zoom = company.zoom
        }
        val launcherIntent = Intent(view, LocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun doCancel() {
        view.finish()
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            }
    }
}
