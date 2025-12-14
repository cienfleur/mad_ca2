package ie.setu.mad_ca2.main

import android.app.Application
import com.google.firebase.auth.FirebaseAuth // Import this
import ie.setu.mad_ca2.models.CompanyJSONStorage
import ie.setu.mad_ca2.models.CompanyFireStoreStorage
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var companies : CompanyFireStoreStorage
    lateinit var auth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        auth = FirebaseAuth.getInstance()
        companies = CompanyFireStoreStorage(applicationContext)
        i("Application started")
    }
}
