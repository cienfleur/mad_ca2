package ie.setu.mad_ca2.views.companylist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.mad_ca2.R
import ie.setu.mad_ca2.adapters.CompanyAdapter
import ie.setu.mad_ca2.adapters.CompanyListener
import ie.setu.mad_ca2.databinding.ActivityListBinding
import ie.setu.mad_ca2.main.MainApp
import ie.setu.mad_ca2.models.Company


class CompanyListView : AppCompatActivity(), CompanyListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityListBinding
    lateinit var presenter: CompanyListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        presenter = CompanyListPresenter(this)

        // Setup RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.companyList.layoutManager = layoutManager
        loadCompanies()
    }

    private fun loadCompanies() {
        binding.companyList.adapter = CompanyAdapter(presenter.getCompanies(), this)
        binding.companyList.adapter?.notifyDataSetChanged()
    }

    // Called by Presenter to force a UI update
    fun showCompanies(companies: List<Company>) {
        binding.companyList.adapter = CompanyAdapter(companies, this)
        binding.companyList.adapter?.notifyDataSetChanged()
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

    override fun onCompanyClick(company: Company) {
        presenter.doEditCompany(company)
    }
}
