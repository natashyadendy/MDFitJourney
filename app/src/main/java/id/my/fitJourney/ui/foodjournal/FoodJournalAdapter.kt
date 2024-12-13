package id.my.fitJourney.ui.foodjournal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.my.fitJourney.R
import id.my.fitJourney.data.local.entity.FoodEntity

class FoodJournalAdapter(
    private val foodList: List<FoodEntity>,
) : RecyclerView.Adapter<FoodJournalAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.food_journal_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foodList[position]
        holder.bind(food)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvType: TextView = itemView.findViewById(R.id.tvType)
        val tvCalories: TextView = itemView.findViewById(R.id.tvCalories)

        fun bind(food: FoodEntity) {
            tvName.text = food.name
            tvType.text = food.type
            tvCalories.text = "Calories: ${food.calories}"
        }
    }
}