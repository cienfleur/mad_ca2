package ie.setu.mad_ca2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.mad_ca2.databinding.CardDisplayBinding
import ie.setu.mad_ca2.models.Company

interface CompanyListener {
    fun onCompanyClick(company: Company)
    // Optional: Add onCompanyDeleteClick(company: Company) here if needed
}

class CompanyAdapter constructor(private var companies: List<Company>, private val listener: CompanyListener) :
    RecyclerView.Adapter<CompanyAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardDisplayBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val company = companies[holder.adapterPosition]
        holder.bind(company, listener)
    }

    override fun getItemCount(): Int = companies.size

    class MainHolder(private val binding : CardDisplayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(company: Company, listener: CompanyListener) {
            // Ensure these IDs match your card_company.xml layout
            binding.name.text = company.name
            binding.description.text = company.description

            // Set the click listener on the whole card
            binding.root.setOnClickListener { listener.onCompanyClick(company) }

            // If you have a delete button in your card XML, uncomment this:
            // binding.btnDelete.setOnClickListener { listener.onCompanyDeleteClick(company) }
        }
    }
}
