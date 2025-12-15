package ie.setu.mad_ca2.views.companylist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.mad_ca2.R
import ie.setu.mad_ca2.adapters.CompanyAdapter
import ie.setu.mad_ca2.adapters.CompanyListener
import ie.setu.mad_ca2.databinding.ActivityListBinding
import ie.setu.mad_ca2.main.MainApp
import ie.setu.mad_ca2.models.Company
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate


class CompanyListView : AppCompatActivity(), CompanyListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityListBinding
    lateinit var presenter: CompanyListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        presenter = CompanyListPresenter(this)

        val layoutManager = LinearLayoutManager(this)
        binding.companyList.layoutManager = layoutManager
        loadCompanies()

        setupBottomNav()
    }

    private fun loadCompanies() {
        binding.companyList.adapter = CompanyAdapter(presenter.getCompanies(), this)
        binding.companyList.adapter?.notifyDataSetChanged()
    }

    fun showCompanies(companies: List<Company>) {
        binding.companyList.adapter = CompanyAdapter(companies, this)
        binding.companyList.adapter?.notifyDataSetChanged()
    }

    fun showDeleteConfirm(company: Company) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Company")
        builder.setMessage("Are you sure you want to delete this company?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            presenter.doConfirmDelete(company)
            loadCompanies()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddCompany() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCompanyClick(company: Company, bindingAdapterPosition: Int) {
        presenter.doEditCompany(company)
    }

    override fun onCompanyDeleteClick(company: Company) {
        presenter.doDeleteCompany(company)
        }
    private fun setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    true
                }
                R.id.item_theme_toggle -> {
                    toggleTheme()
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
