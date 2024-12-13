package id.my.fitJourney.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SuggestionItem(
    @SerializedName("Name") val name: String,
    @SerializedName("Calories") val calories: Double
): Parcelable