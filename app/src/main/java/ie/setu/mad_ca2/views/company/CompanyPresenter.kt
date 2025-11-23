package ie.setu.mad_ca2.views.company

import ie.setu.mad_ca2.models.Company
import ie.setu.mad_ca2.main.MainApp
class CompanyPresenter(val view: CompanyView) {

    var company = Company()
    var app: MainApp = view.application as MainApp


    fun doAddCompany(name: String, description: String, country: String, date: String) {
        val company = Company()
        company.name = name
        company.description = description
        company.country = country
        company.date = date
        app.companies.create(company)
        // view.showCompanyList()
        
        // Logic to add company to storage will go here

    }
}
