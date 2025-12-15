package ie.setu.mad_ca2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.mad_ca2.R
import ie.setu.mad_ca2.databinding.CardDisplayBinding
import ie.setu.mad_ca2.models.Company
import java.text.SimpleDateFormat
import java.util.*


interface CompanyListener {
    fun onCompanyClick(company: Company, bindingAdapterPosition: Int)
    fun onCompanyDeleteClick(company: Company)
}

class CompanyAdapter constructor(private var companies: List<Company>, private val listener: CompanyListener) :
    RecyclerView.Adapter<CompanyAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardDisplayBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val company = companies[holder.bindingAdapterPosition]
        holder.bind(company, listener)
    }

    override fun getItemCount(): Int = companies.size

    class MainHolder(private val binding : CardDisplayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(company: Company, listener: CompanyListener) {
            val date = Date(company.date)
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = formatter.format(date)
            binding.name.text = company.name
            binding.description.text = company.description
            binding.country.text = company.country
            binding.date.text = formattedDate
            if (!company.image.toString().isEmpty()) {
                Picasso.get().load(company.image).resize(200,200).into(binding.imageIcon)
            } else {
                binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            }
            binding.root.setOnClickListener { listener.onCompanyClick(company,bindingAdapterPosition) }
            binding.btnDelete.setOnClickListener { listener.onCompanyDeleteClick(company) }
        }
    }
}
