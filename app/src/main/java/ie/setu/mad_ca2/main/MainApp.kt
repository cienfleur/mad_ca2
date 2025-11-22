package ie.setu.mad_ca2.main

import android.app.Application
import ie.setu.mad_ca2.models.CompanyJSONStorage
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var companies : CompanyJSONStorage


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        companies = CompanyJSONStorage(applicationContext)
        i("Application started")
    }
}
