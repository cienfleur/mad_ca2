package ie.setu.mad_ca2.models

interface CompanyStorage {
    fun findAll(): List<Company>
    fun findById(id:Long) : Company?
    fun create(placemark: Company)
    fun update(placemark: Company)
    fun delete(placemark: Company)
}