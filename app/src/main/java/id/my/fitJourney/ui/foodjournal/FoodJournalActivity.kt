package id.my.fitJourney.ui.foodjournal

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.my.fitJourney.R
import id.my.fitJourney.ViewModelFactory
import id.my.fitJourney.databinding.ActivityFoodJournalBinding
import id.my.fitJourney.ui.detailfood.DetailFoodActivity
import id.my.fitJourney.ui.detailfood.DetailFoodViewModel

class FoodJournalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodJournalBinding
    private lateinit var viewModel: FoodJournalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this)

        setupBinding()
        setupList()
    }

    private fun setupBinding() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupList() {
        viewModel.foodList.observe(this) {
            val adapter = FoodJournalAdapter(it)
            binding.rvFoodJournal.adapter = adapter
            binding.rvFoodJournal.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun obtainViewModel(activity: FoodJournalActivity): FoodJournalViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FoodJournalViewModel::class.java]
    }
}