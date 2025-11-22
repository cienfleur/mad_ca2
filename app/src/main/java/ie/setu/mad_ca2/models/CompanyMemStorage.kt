package ie.setu.mad_ca2.models

import timber.log.Timber.i
import kotlin.collections.find
import kotlin.collections.forEach

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class CompanyMemStorage : CompanyStorage {

    val placemarks = kotlin.collections.ArrayList<Company>()

    override fun findAll(): List<Company> {
        return placemarks
    }

    override fun findById(id:Long) : Company? {
        val foundPlacemark: Company? = placemarks.find { it.id == id }
        return foundPlacemark
    }

    override fun create(placemark: Company) {
        placemark.id = getId()
        placemarks.add(placemark)
        logAll()
    }

    override fun update(placemark: Company) {
        val foundPlacemark: Company? = placemarks.find { p -> p.id == placemark.id }
        if (foundPlacemark != null) {
            foundPlacemark.title = placemark.title
            foundPlacemark.description = placemark.description
            foundPlacemark.image = placemark.image
            foundPlacemark.lat = placemark.lat
            foundPlacemark.lng = placemark.lng
            foundPlacemark.zoom = placemark.zoom
            logAll()
        }
    }

    private fun logAll() {
        placemarks.forEach { i("$it") }
    }

    override fun delete(placemark: Company) {
        placemarks.remove(placemark)
    }
}