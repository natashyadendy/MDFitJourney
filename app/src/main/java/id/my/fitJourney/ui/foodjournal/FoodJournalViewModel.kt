package id.my.fitJourney.ui.foodjournal

import androidx.lifecycle.ViewModel
import id.my.fitJourney.data.repository.Repository

class FoodJournalViewModel(
    private val repository: Repository
): ViewModel() {
    val foodList = repository.getAllFood()
}