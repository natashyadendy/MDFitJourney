package id.my.fitJourney.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import id.my.fitJourney.data.local.dao.FavoriteFoodDao
import id.my.fitJourney.data.local.dao.FoodDao
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.data.local.entity.FoodEntity
import id.my.fitJourney.data.remote.model.RegisterModel
import id.my.fitJourney.data.remote.response.RegisterResponse
import id.my.fitJourney.data.remote.retrofit.CCApiService
import id.my.fitJourney.data.remote.Result
import id.my.fitJourney.data.remote.model.CustomFoodRequest
import id.my.fitJourney.data.remote.model.SuggestionItem
import id.my.fitJourney.data.remote.response.CustomFoodResponseItem
import id.my.fitJourney.data.remote.response.ErrorResponse
import id.my.fitJourney.data.remote.retrofit.ML2ApiService
import id.my.fitJourney.data.remote.retrofit.MLApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class Repository private constructor(
    private val ccApiService: CCApiService,
    private val ccAuthApiService: CCApiService,
    private val mlApiService: MLApiService,
    private val ml2ApiService: ML2ApiService,
    private val favoriteFoodDao: FavoriteFoodDao,
    private val foodDao: FoodDao
) {
    suspend fun insertFoodData(food: FoodData, type: String) {
        return withContext(Dispatchers.IO) {
            val foodEntity = FoodEntity(
                id = food.id,
                type = type,
                name = food.name,
                description = food.description,
                images = food.images,
                recipeIngredientQuantities = food.recipeIngredientQuantities,
                recipeIngredientParts = food.recipeIngredientParts,
                recipeInstructions = food.recipeInstructions,
                recipeServings = food.recipeServings,
                recipeCategory = food.recipeCategory,
                totalTime = food.totalTime,
                sugarContent = food.sugarContent,
                cholesterolContent = food.cholesterolContent,
                saturatedFatContent = food.saturatedFatContent,
                proteinContent = food.proteinContent,
                sodiumContent = food.sodiumContent,
                calories = food.calories,
                carbohydrateContent = food.carbohydrateContent,
                fatContent = food.fatContent,
                fiberContent = food.fiberContent
            )
            foodDao.insert(foodEntity)
        }
    }

    fun getAllFood(): LiveData<List<FoodEntity>> = foodDao.getAllFood()

    fun getAllFoodByType(type: String): LiveData<List<FoodEntity>> = foodDao.getAllFoodByType(type)

    fun postRegister(registerData: RegisterModel): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response =
                ccAuthApiService.register(registerData)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Log.d(Repository::class.java.simpleName, "postRegister: ${e.message.toString()}")
            emit(Result.Error(errorMessage))
        }
    }


    fun postCustomFood(
        calories: Int
    ): LiveData<Result<List<CustomFoodResponseItem>>> = liveData {
        emit(Result.Loading)
        try {
            val customFoodRequest = CustomFoodRequest(
                calories,
            )
            val response = mlApiService.customFood(customFoodRequest)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Log.d(Repository::class.java.simpleName, "postRegister: ${e.message.toString()}")
            emit(Result.Error(errorMessage))
        }
    }

    fun getFavoriteFoods(): LiveData<List<FoodData>> = favoriteFoodDao.getAllFavorites()

    suspend fun deleteFavoriteFoodsById(name: String) {
        favoriteFoodDao.delete(name)
    }

    suspend fun setFavoriteFoods(foodData: FoodData) {
        favoriteFoodDao.insert(foodData)
    }

    fun isFoodFavorite(name: String): LiveData<Int> = favoriteFoodDao.isFoodFavoriteByName(name)

    fun getSugestionFood(body: HashMap<String, String>): LiveData<Result<List<SuggestionItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = ml2ApiService.getSuggestionFood(body)
            if (response.isNotEmpty()) {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            ccApiService: CCApiService,
            ccAuthApiService: CCApiService,
            mlApiService: MLApiService,
            ml2ApiService: ML2ApiService,
            favoriteFoodDao: FavoriteFoodDao,
            foodDao: FoodDao
        ): Repository = instance ?: synchronized(this) {
            instance ?: Repository(ccApiService, ccAuthApiService, mlApiService, ml2ApiService, favoriteFoodDao, foodDao)
        }.also { instance = it }
    }
}