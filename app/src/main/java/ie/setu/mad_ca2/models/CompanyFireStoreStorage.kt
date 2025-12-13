package ie.setu.mad_ca2.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import timber.log.Timber

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
            // Generate a new ID from Firestore automatically
            val newRef = db.collection("users").document(userId).collection("companies").document()
            company.id = newRef.id

            newRef.set(company)
                .addOnSuccessListener {
                    companies.add(company)
                    Timber.i("Firestore: Company Created ${company.id}")
                }
                .addOnFailureListener { e ->
                    Timber.e(e, "Firestore: Error creating company")
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
                    Timber.i("Firestore: Company Updated ${company.id}")
                }
                .addOnFailureListener { e ->
                    Timber.e(e, "Firestore: Error updating company")
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
                    Timber.i("Firestore: Company Deleted ${company.id}")
                }
                .addOnFailureListener { e ->
                    Timber.e(e, "Firestore: Error deleting company")
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
                        // Ensure the ID is set correctly from the document ID
                        company.id = document.id
                        companies.add(company)
                    }
                    onComplete()
                }
                .addOnFailureListener { exception ->
                    Timber.e(exception, "Firestore: Error getting documents.")
                    onComplete() // Callback even on failure so the app doesn't hang
                }
        } else {
            Timber.e("Firestore: User not logged in")
            onComplete()
        }
    }

    override fun findById(id:String) : Company? {
        return companies.find { it.id == id }
    }
}
