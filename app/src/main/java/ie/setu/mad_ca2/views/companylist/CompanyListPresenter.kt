package ie.setu.mad_ca2.views.companylist

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import ie.setu.mad_ca2.main.MainApp
import ie.setu.mad_ca2.models.Company
import ie.setu.mad_ca2.views.company.CompanyView
import timber.log.Timber.i


class CompanyListPresenter(val view: CompanyListView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    init {
        app.companies.fetchCompanies { loadCompanies() }
        registerRefreshCallback()
    }

    fun getCompanies() = app.companies.findAll()

    fun doAddCompany() {
        val launcherIntent = Intent(view, CompanyView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doEditCompany(company: Company) {
        i("Company to edit: $company")
        val launcherIntent = Intent(view, CompanyView::class.java)
        launcherIntent.putExtra("company_edit", company)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doDeleteCompany(company: Company) {
        view.showDeleteConfirm(company)
    }

    fun doConfirmDelete(company: Company) {
        i("Deleting company: $company")
        app.companies.delete(company)
        loadCompanies()
    }

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
