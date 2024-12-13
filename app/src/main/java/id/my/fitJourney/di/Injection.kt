package id.my.fitJourney.di

import android.content.Context
import id.my.fitJourney.data.local.database.FavoriteFoodDatabase
import id.my.fitJourney.data.local.database.FoodDatabase
import id.my.fitJourney.data.remote.retrofit.ApiConfig
import id.my.fitJourney.data.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val ccApiService = ApiConfig.getCCApiService()
        val ccAuthApiService = ApiConfig.getCCApiServiceForAuth()
        val mlApiService = ApiConfig.getMLApiService()
        val ml2ApiService = ApiConfig.getML2ApiService()
        val favoriteDatabase = FavoriteFoodDatabase.getDatabase(context)
        val favoriteFoodDao = favoriteDatabase.favoriteFoodDao()
        val foodDatabase = FoodDatabase.getDatabase(context)
        val foodDao = foodDatabase.foodDao()
        return Repository.getInstance(ccApiService, ccAuthApiService, mlApiService, ml2ApiService, favoriteFoodDao, foodDao)
    }
}