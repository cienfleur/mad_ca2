package ie.setu.mad_ca2.models

import timber.log.Timber.i
import kotlin.collections.find
import kotlin.collections.forEach

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class CompanyMemStorage : CompanyStorage {

    val companies = kotlin.collections.ArrayList<Company>()

    override fun findAll(): List<Company> {
        return companies
    }

    override fun findById(id:Long) : Company? {
        val foundCompany: Company? = companies.find { it.id == id }
        return foundCompany
    }

    override fun create(company: Company) {
        company.id = getId()
        companies.add(company)
        logAll()
    }

    override fun update(company: Company) {
        val foundCompany: Company? = companies.find { p -> p.id == company.id }
        if (foundCompany != null) {
            foundCompany.name = company.name
            foundCompany.description = company.description
            foundCompany.image = company.image
            foundCompany.lat = company.lat
            foundCompany.lng = company.lng
            foundCompany.zoom = company.zoom
            logAll()
        }
    }

    private fun logAll() {
        companies.forEach { i("$it") }
    }

    override fun delete(company: Company) {
        companies.remove(company)
    }
}