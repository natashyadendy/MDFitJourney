package id.my.fitJourney.ui.customfoodresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.data.local.entity.FoodEntity
import id.my.fitJourney.data.repository.Repository
import kotlinx.coroutines.launch

class CustomFoodResultViewModel(private val repository: Repository) : ViewModel() {

    private val _search = MutableLiveData<String>()
    val search: LiveData<String> get() = _search
    val getAllFoodData: LiveData<List<FoodEntity>> = repository.getAllFood() // Fungsi baru untuk mengambil semua data makanan

    fun onSearchChanged(search: String) {
        _search.value = search
    }

    fun saveFavoriteFood(food: FoodData) {
        viewModelScope.launch {
            repository.setFavoriteFoods(food)
        }
    }

    fun deleteFavorite(name: String) {
        viewModelScope.launch {
            repository.deleteFavoriteFoodsById(name)
        }
    }

    fun isFoodFavorite(name: String): LiveData<Int> = repository.isFoodFavorite(name)
}