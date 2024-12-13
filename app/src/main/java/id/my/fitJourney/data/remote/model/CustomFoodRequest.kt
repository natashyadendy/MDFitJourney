package id.my.fitJourney.data.remote.model

import com.google.gson.annotations.SerializedName

data class CustomFoodRequest(
    @SerializedName("Calories") val calories: Int
)

