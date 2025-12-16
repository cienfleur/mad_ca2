package ie.setu.mad_ca2.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import timber.log.Timber.i

class CompanyFireStoreStorage(val context: Context) : CompanyStorage {

    private var companies = ArrayList<Company>()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun findAll(): List<Company> {
        return companies
    }

    override fun create(company: Company) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val newRef = db.collection("users").document(userId).collection("companies").document()
            company.id = newRef.id

            newRef.set(company)
                .addOnSuccessListener {
                    companies.add(company)
                    i("Firestore: Company Created ${company.id}")
                }
                .addOnFailureListener { e ->
                    i("Firestore: Error creating company")
                }
        }
    }

    override fun update(company: Company) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val docRef = db.collection("users").document(userId).collection("companies").document(company.id)

            docRef.set(company)
                .addOnSuccessListener {
                    val index = companies.indexOfFirst { it.id == company.id }
                    if (index != -1) {
                        companies[index] = company
                    }
                    i("Firestore: Company Updated ${company.id}")
                }
                .addOnFailureListener { e ->
                    i("Firestore: Error updating company")
                }
        }
    }

    override fun delete(company: Company) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).collection("companies").document(company.id)
                .delete()
                .addOnSuccessListener {
                    companies.remove(company)
                    i("Firestore: Company Deleted ${company.id}")
                }
                .addOnFailureListener { e ->
                    i("Firestore: Error deleting company")
                }
        }
    }


    fun fetchCompanies(onComplete: () -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).collection("companies")
                .get()
                .addOnSuccessListener { result ->
                    companies.clear()
                    for (document in result) {
                        val company = document.toObject<Company>()
                        company.id = document.id
                        companies.add(company)
                    }
                    onComplete()
                }
                .addOnFailureListener { exception ->
                    i(exception, "Firestore: Error getting documents.")
                    onComplete()
                }
        } else {
            i("Firestore: User not logged in")
            onComplete()
        }
    }

    override fun findById(id:String) : Company? {
        return companies.find { it.id == id }
    }
}
