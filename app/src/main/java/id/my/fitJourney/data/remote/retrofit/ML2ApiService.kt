package id.my.fitJourney.data.remote.retrofit

import id.my.fitJourney.data.remote.model.CustomFoodRequest
import id.my.fitJourney.data.remote.model.SuggestionItem
import id.my.fitJourney.data.remote.response.CustomFoodResponseItem
import retrofit2.http.Body
import retrofit2.http.POST

interface ML2ApiService {

    @POST("getRecomend")
    suspend fun getSuggestionFood(@Body data: HashMap<String, String>): List<SuggestionItem>

}

interface MLApiService {
    @POST("form")
    suspend fun customFood(@Body request: CustomFoodRequest): List<CustomFoodResponseItem>

}