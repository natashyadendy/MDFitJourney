package id.my.fitJourney.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import id.my.fitJourney.data.remote.model.SuggestionItem
import id.my.fitJourney.databinding.SuggestionItemBinding

class SuggestionAdapter: Adapter<SuggestionAdapter.SuggestionHolder>() {
    private var data: List<SuggestionItem> = ArrayList()

    fun Update(data: List<SuggestionItem>){
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionHolder {
        val binding = SuggestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuggestionHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: SuggestionHolder, position: Int) {
        holder.bind(data[position])
    }

    class SuggestionHolder(private val v: SuggestionItemBinding): ViewHolder(v.root){
        fun bind(item: SuggestionItem){
            v.btnSuggestionItem.text = "${item.name} | ${item.calories} ccal"
        }
    }
}