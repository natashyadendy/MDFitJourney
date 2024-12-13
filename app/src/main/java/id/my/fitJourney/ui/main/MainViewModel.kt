package id.my.fitJourney.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.data.remote.Result
import id.my.fitJourney.data.remote.model.SuggestionItem
import id.my.fitJourney.data.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    fun suggestionFood(
        gender: String,
        height: Int,
        weight: Int,
        age: Int,
        activity: String,
        objective: String,
        ): LiveData<Result<List<SuggestionItem>>>{
        val body = HashMap<String, String>()
        body["gender"] = gender
        body["height"] = height.toString()
        body["weight"] = weight.toString()
        body["age"] = age.toString()
        body["activity"] = activity
        body["objective"] = objective

        return repository.getSugestionFood(body)
    }
}