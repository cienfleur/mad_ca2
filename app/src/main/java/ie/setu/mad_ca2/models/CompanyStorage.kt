package ie.setu.mad_ca2.models

interface CompanyStorage {
    fun findAll(): List<Company>
    fun findById(id:String) : Company?
    fun create(company: Company)
    fun update(company: Company)
    fun delete(company: Company)
}