package ie.setu.mad_ca2.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.setu.mad_ca2.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.find
import kotlin.collections.forEach
import kotlin.jvm.java
import kotlin.toString

const val JSON_FILE = "stored_data.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<Company>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class CompanyJSONStorage(private val context: Context) : CompanyStorage {

    var companies = mutableListOf<Company>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<Company> {
        logAll()
        return companies
    }

    override fun findById(id:Long) : Company? {
        val foundPlacemark: Company? = companies.find { it.id == id }
        return foundPlacemark
    }

    override fun create(placemark: Company) {
        placemark.id = generateRandomId()
        companies.add(placemark)
        serialize()
    }

    override fun update(placemark: Company) {
        val placemarksList = findAll() as ArrayList<Company>
        var foundPlacemark: Company? = placemarksList.find { p -> p.id == placemark.id }
        if (foundPlacemark != null) {
            foundPlacemark.title = placemark.title
            foundPlacemark.description = placemark.description
            foundPlacemark.image = placemark.image
            foundPlacemark.lat = placemark.lat
            foundPlacemark.lng = placemark.lng
            foundPlacemark.zoom = placemark.zoom
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(companies, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        companies = gsonBuilder.fromJson(jsonString, listType)
    }

    override fun delete(placemark: Company) {
        companies.remove(placemark)
        serialize()
    }

    private fun logAll() {
        companies.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}