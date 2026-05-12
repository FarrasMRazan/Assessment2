package com.farrasmuhammadrazan0100.assement2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.farrasmuhammadrazan0100.assement2.model.Manhwa
import com.farrasmuhammadrazan0100.assement2.model.Character

@Database(
    entities = [Manhwa::class, Character::class],
    version = 1,
    exportSchema = false
)
abstract class ManhwaDb : RoomDatabase() {
    abstract val manhwaDao: ManhwaDao
    abstract val characterDao: CharacterDao

    companion object {
        @Volatile
        private var INSTANCE: ManhwaDb? = null

        fun getDatabase(context: Context): ManhwaDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ManhwaDb::class.java,
                    "manhwa_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}