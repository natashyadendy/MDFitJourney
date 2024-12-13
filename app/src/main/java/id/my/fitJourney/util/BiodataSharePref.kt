package id.my.fitJourney.util

import android.content.Context
import android.content.SharedPreferences

class BiodataSharePref(context: Context) {
    private val sharePref: SharedPreferences = context.getSharedPreferences("BIODATA_PREF", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharePref.edit()

    companion object{
        const val GENDER = "gender"
        const val AGE = "age"
        const val HEIGHT = "height"
        const val WEIGHT = "weight"
        const val LEVEL = "level"
        const val GOAL = "goal"
    }

    fun saveGender(gender: String){
        editor.putString(GENDER, gender)
        editor.apply()
        editor.commit()
    }

    fun saveAge(age: String){
        editor.putString(AGE, age)
        editor.apply()
        editor.commit()
    }

    fun saveHeight(height: String){
        editor.putString(HEIGHT, height)
        editor.apply()
        editor.commit()
    }

    fun saveWeight(weight: String){
        editor.putString(WEIGHT, weight)
        editor.apply()
        editor.commit()
    }

    fun saveLevel(level: String){
        editor.putString(LEVEL, level)
        editor.apply()
        editor.commit()
    }

    fun saveGoal(goal: String){
        editor.putString(GOAL, goal)
        editor.apply()
        editor.commit()
    }

    fun get(): Map<String, String>{
        return mapOf(
            GENDER to sharePref.getString(GENDER, null).toString(),
            AGE to sharePref.getString(AGE, null).toString(),
            HEIGHT to sharePref.getString(HEIGHT, null).toString(),
            WEIGHT to sharePref.getString(WEIGHT, null).toString(),
            LEVEL to sharePref.getString(LEVEL, null).toString(),
            GOAL to sharePref.getString(GOAL, null).toString(),
        )
    }

    fun isExists(): Boolean{
        return sharePref.getString(GENDER, null) != null &&
                sharePref.getString(AGE, null) != null &&
                sharePref.getString(HEIGHT, null) != null &&
                sharePref.getString(WEIGHT, null) != null &&
                sharePref.getString(LEVEL, null) != null &&
                sharePref.getString(GOAL, null) != null
    }

    fun remove(){
        editor.remove(GENDER)
        editor.remove(AGE)
        editor.remove(HEIGHT)
        editor.remove(WEIGHT)
        editor.remove(LEVEL)
        editor.remove(GOAL)
        editor.apply()
        editor.commit()
    }

}