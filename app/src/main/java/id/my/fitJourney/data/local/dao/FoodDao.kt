package id.my.fitJourney.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.data.local.entity.FoodEntity

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: FoodEntity)

    @Query("SELECT * from food_entity ORDER BY id DESC")
    fun getAllFood(): LiveData<List<FoodEntity>>

    @Query("SELECT * FROM food_entity WHERE type = :type")
    fun getAllFoodByType(type: String): LiveData<List<FoodEntity>>
}