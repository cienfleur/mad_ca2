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
        val foundCompany: Company? = companies.find { it.id == id }
        return foundCompany
    }

    override fun create(company: Company) {
        company.id = generateRandomId()
        companies.add(company)
        serialize()
    }

    override fun update(company: Company) {
        val companyList = findAll() as ArrayList<Company>
        var foundCompany: Company? = companyList.find { p -> p.id == company.id }
        if (foundCompany != null) {
            foundCompany.name = company.name
            foundCompany.description = company.description
            foundCompany.image = company.image
            foundCompany.lat = company.lat
            foundCompany.lng = company.lng
            foundCompany.zoom = company.zoom
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

    override fun delete(company: Company) {
        companies.remove(company)
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