package ie.setu.mad_ca2.views.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import ie.setu.mad_ca2.R
import ie.setu.mad_ca2.databinding.ActivityMapsBinding
import ie.setu.mad_ca2.databinding.MapDisplayBinding
import ie.setu.mad_ca2.main.MainApp
import ie.setu.mad_ca2.models.Company

class MapView : AppCompatActivity() , GoogleMap.OnMarkerClickListener{

    private lateinit var binding: ActivityMapsBinding
    private lateinit var contentBinding: MapDisplayBinding
    lateinit var app: MainApp
    lateinit var presenter: MapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        presenter = MapPresenter(this)

        contentBinding = MapDisplayBinding.bind(binding.root)
        setupBottomNav()

        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync{
            presenter.doPopulateMap(it)

        }
    }
    fun showCompany(company: Company) {
        contentBinding.currentTitle.text = company.name
        contentBinding.currentDescription.text = company.description
        Picasso.get()
            .load(company.image)
            .into(contentBinding.currentImage)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }
    private fun setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    presenter.doShowCompaniesList()
                    true
                }
                R.id.item_theme_toggle -> {
                    toggleTheme()
                    true
                }
                R.id.item_map -> {
                    true
                }
                else -> false
            }
        }
    }
    private fun toggleTheme() {
        val currentMode = AppCompatDelegate.getDefaultNightMode()

        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}