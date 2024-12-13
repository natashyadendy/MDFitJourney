package id.my.fitJourney.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import id.my.fitJourney.data.local.dao.FavoriteFoodDao
import id.my.fitJourney.data.local.dao.FoodDao
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.data.local.entity.FoodEntity
import id.my.fitJourney.util.ConverterUtils

@Database(
    entities = [FoodEntity::class],
    version = 1
)
@TypeConverters(ConverterUtils::class)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao

    companion object {
        @Volatile
        private var INSTANCE: FoodDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FoodDatabase {
            if (INSTANCE == null) {
                synchronized(FoodDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FoodDatabase::class.java, "food"
                    )
                        .build()
                }
            }
            return INSTANCE as FoodDatabase
        }
    }
}