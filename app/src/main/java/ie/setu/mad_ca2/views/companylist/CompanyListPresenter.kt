package ie.setu.mad_ca2.views.companylist

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import ie.setu.mad_ca2.main.MainApp
import ie.setu.mad_ca2.models.Company
import ie.setu.mad_ca2.views.company.CompanyView

class CompanyListPresenter(val view: CompanyListView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    init {
        registerRefreshCallback()
    }

    fun getCompanies() = app.companies.findAll()

    fun doAddCompany() {
        val launcherIntent = Intent(view, CompanyView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doEditCompany(company: Company) {
        val launcherIntent = Intent(view, CompanyView::class.java)
        launcherIntent.putExtra("company_edit", company)
        refreshIntentLauncher.launch(launcherIntent)
    }

    // Since we don't have direct access to the adapter here,
    // we simply ask the view to refresh everything when we return.
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                loadCompanies()
            }
    }

    fun loadCompanies() {
        view.showCompanies(app.companies.findAll())
    }
}
